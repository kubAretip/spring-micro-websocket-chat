db.createUser(
    {
        user : "chat-service-user",
        pwd : "chat-service-pass-123",
        roles : [
            {
                role: "readWrite",
                db : "chat_service_database"
            }
        ]
    }
);