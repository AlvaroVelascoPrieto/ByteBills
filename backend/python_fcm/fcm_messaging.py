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
            title='Comprueba tus stocks',
            body='Descubre qué está pasando en el mercado',
        ),
        tokens=tokens,
    )

    response = messaging.send_multicast(message)
    print('Successfully sent message:', response)

while 1:
    send_message()
    sleep(3600*12)