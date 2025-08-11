package com.lcwd.gateway.config;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.EmptyResponse;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;

import reactor.core.publisher.Mono;

public class CustomLoadBalancer implements ReactorServiceInstanceLoadBalancer {

	private final ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;
    private final String serviceId;
    private final Random random;
    private static final int TIMEOUT_MS = 500;
    
    public CustomLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider, 
            String serviceId) {
		this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
		this.serviceId = serviceId;
		this.random = new Random();
	}

    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
    	System.out.println("----------Load Balancer---------:"+serviceId);
        ServiceInstanceListSupplier supplier = serviceInstanceListSupplierProvider.getIfAvailable();
        return supplier.get()
                .next()
                .map(this::getInstanceResponse);
    }

    private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> instances) {
        if (instances.isEmpty()) {
            return new EmptyResponse();
        }
        
        System.out.println("Available instances for service [" + serviceId + "]:");
        for (ServiceInstance instance : instances) {
            System.out.println(" - Host: " + instance.getHost()
                    + ", Port: " + instance.getPort()
                    + ", Service ID: " + instance.getServiceId()
                    + ", URI: " + instance.getUri()
                    + ", Metadata: " + instance.getMetadata());
        }
        
        ExecutorService executor = Executors.newCachedThreadPool();
        
        List<CompletableFuture<Optional<ServiceInstance>>> futures = new ArrayList<>();

        for (ServiceInstance instance : instances) {
            futures.add(checkHealthAsync(instance,executor));
        }

        // Wait for all futures to complete within TIMEOUT_MS
        List<ServiceInstance> healthyInstances = futures.stream()
        	    .map(new Function<CompletableFuture<Optional<ServiceInstance>>, Optional<ServiceInstance>>() {
        	        @Override
        	        public Optional<ServiceInstance> apply(CompletableFuture<Optional<ServiceInstance>> future) {
        	            try {
        	                return future.get(TIMEOUT_MS, TimeUnit.MILLISECONDS);
        	            } catch (Exception e) {
        	                return Optional.empty();
        	            }
        	        }
        	    })
        	    .filter(Optional::isPresent)
        	    .map(Optional::get)
        	    .collect(Collectors.toList());

        if (healthyInstances.isEmpty()) {
            System.out.println("No healthy instances found within timeout.");
            return new EmptyResponse();
        }else {
        	System.out.println("****Total healthy instances*** :"+healthyInstances.size());
        }
        // Implement your custom load balancing logic here
        ServiceInstance instance = healthyInstances.get(random.nextInt(healthyInstances.size()));
        
        executor.shutdown();
        try {
            if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        return new DefaultResponse(instance);
    }
    
    private CompletableFuture<Optional<ServiceInstance>> checkHealthAsync(ServiceInstance instance,
    		ExecutorService executor) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String healthUrl = instance.getUri().toString() + "/actuator/health";
                URI uri = URI.create(healthUrl);
                URL url = uri.toURL();
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(300); // Connection timeout
                conn.setReadTimeout(300);    // Read timeout
                conn.setRequestMethod("GET");

                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    Scanner scanner = new Scanner(conn.getInputStream()).useDelimiter("\\A");
                    String response = scanner.hasNext() ? scanner.next() : "";
                    if (response.contains("\"status\":\"UP\"")) {
                        return Optional.of(instance);
                    }
                }
            } catch (Exception e) {
                // log exception or ignore
            }
            return Optional.empty();
        }, executor);
    }
    
    // Example of weighted random selection
    private ServiceInstance getWeightedRandomInstance(List<ServiceInstance> instances) {
        int totalWeight = instances.stream()
                .mapToInt(instance -> 
                    Integer.parseInt(instance.getMetadata().getOrDefault("weight", "1")))
                .sum();
        
        int randomWeight = random.nextInt(totalWeight);
        int currentWeight = 0;
        
        for (ServiceInstance instance : instances) {
            int instanceWeight = Integer.parseInt(
                instance.getMetadata().getOrDefault("weight", "1"));
            currentWeight += instanceWeight;
            if (randomWeight < currentWeight) {
                return instance;
            }
        }
        
        return instances.get(0);
    }
}
