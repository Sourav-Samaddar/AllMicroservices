db = db.getSiblingDB("microservicesdb");

db.createUser({
  user: "appuser",
  pwd: "app123",
  roles: [
    { role: "readWrite", db: "microservicesdb" }
  ]
});
