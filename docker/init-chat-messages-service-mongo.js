db.createUser(
    {
        user : "chat-messages-service-user",
        pwd : "chat-messages-service-pass-123",
        roles : [
            {
                role: "readWrite",
                db : "chat_messages_service_database"
            }
        ]
    }
);