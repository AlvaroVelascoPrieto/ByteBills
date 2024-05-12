from flask import jsonify
import mysql.connector

#Aqui se hacen las conexiones con la BD y se devuelve el diccionario Python que indica el estado de la ejecucion SQL

def get_db_connection():
    return mysql.connector.connect(
        host="db",
        user="root",
        password="root",
        database="bytebills"
    )

#################
## USER ROUTES ##
#################

def db_register(data):
    username = data['username']
    email = data['email']
    password = data['password']

    db = get_db_connection()
    cursor = db.cursor()
    try:
        cursor.execute("INSERT INTO users (username, email, password) VALUES (%s, %s, %s)", (username, email, password))
        db.commit()
        response = {'status': 'Ok'}
        return response

    except mysql.connector.Error as err:
        response = {'status': err.msg}
        return response

def db_login(data):
    username = data['username']
    password = data['password']

    db = get_db_connection()
    cursor = db.cursor()
    cursor.execute("SELECT * FROM users WHERE username=%s AND password=%s", (username, password))
    user = cursor.fetchone()
    try:
        if user:
            response = {'status': 'Ok'}
            return response
        else:
            response = {'status': 'Not Found'}
            return response
    except mysql.connector.Error as err:
        response = {'status': err.msg}
        return response

##################
## STOCK ROUTES ##
##################

def db_add_stock_to_user(data):
    symbol = data['symbol']
    username = data['username']
    db = get_db_connection()
    cursor = db.cursor()
    cursor.execute("SELECT * FROM stocks WHERE symbol = %s", data['symbol'])
    stock = cursor.fetchone()
    if not stock:
        cursor.execute("INSERT INTO stocks VALUES (%s, %s)", symbol, "placeholder nombre compañía")

    try:
        cursor.execute("INSERT INTO stock_user (username, stock_symbol) VALUES (%s, %s)", (username, symbol))
        db.commit()
        response = {'status': 'Ok'}
        return response

    except mysql.connector.Error as err:
        response = {'status': err.msg}
        return response