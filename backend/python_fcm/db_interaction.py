import mysql.connector
        
def get_db_connection():
    return mysql.connector.connect(
        host="db",
        user="root",
        password="root",
        database="bytebills"
    )

def db_get_all_fcm_tokens():
    db = get_db_connection()
    cursor = db.cursor()
    try:
        cursor.execute("SELECT fcm_token FROM users")
        tokens = cursor.fetchall()
        return tokens
    except mysql.connector.Error as err:
        return {'status': err.msg}