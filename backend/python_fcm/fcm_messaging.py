import db_interaction
import firebase_admin
from firebase_admin import credentials
from firebase_admin import messaging
from time import sleep

cred = credentials.Certificate("./bytebills-firebase-key.json")
firebase_admin.initialize_app(cred)

def send_message():
    tokens = db_interaction.db_get_all_fcm_tokens()
    tokens = [x[0] for x in tokens]

    message = messaging.MulticastMessage(
        notification=messaging.Notification(
            title='Your title',
            body='Your body text',
        ),
        tokens=tokens,
    )

    response = messaging.send_multicast(message)
    print('Successfully sent message:', response)

sleep(5)
send_message()