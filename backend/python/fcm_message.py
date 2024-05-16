import db_interaction
import firebase_admin
from firebase_admin import credentials
from firebase_admin import messaging

cred = credentials.Certificate("./bytebills-firebase-key.json")
firebase_admin.initialize_app(cred)

tokens = db_interaction.db_get_all_fcm_tokens()
tokens = [x[0] for x in tokens]

message = messaging.MulticastMessage(
    notification=messaging.Notification(
        title='Your title',
        body='Your body text',
    ),
    tokens=registration_tokens,
)

# Send the message.
response = messaging.send_multicast(message)

# Response is a message ID string.
print('Successfully sent message:', response)