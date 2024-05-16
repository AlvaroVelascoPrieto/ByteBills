from flask import jsonify
import mysql.connector
import logging
import utils
import financeData
import json

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
    
    print("DATA ========== \n\n\n\n")
    print(data)
    print("\n\n\n\nDATA ========== ")

    symbol = data['symbol']
    username = data['username']
    stock_name = data['name']
    db = get_db_connection()
    cursor = db.cursor()
    cursor.execute("SELECT * FROM stocks WHERE symbol =%s", (symbol,))
    stock = cursor.fetchone()
    if not stock:
        cursor.execute("INSERT INTO stocks VALUES (%s, %s)", (symbol, stock_name))

    try:
        cursor.execute("INSERT INTO stock_user (username, stock_symbol) VALUES (%s, %s)", (username, symbol))
        db.commit()
        response = {'status': 'Ok'}
        return response
    except mysql.connector.IntegrityError:
        response = {'status': 'Ok'}
        return response

    except mysql.connector.Error as err:
        response = {'status': err.msg}
        return response

def db_delete_stock_user(data):
    print("DELETE STOCK USER")
    print("DATA: ", data)
    symbol = data['symbol']
    username = data['username']
    db = get_db_connection()
    cursor = db.cursor()
    try:
        cursor.execute("DELETE FROM stock_user WHERE username=%s AND stock_symbol=%s", (username, symbol))
        db.commit()
        response = {'status': 'Ok'}
        return response

    except mysql.connector.Error as err:
        response = {'status': err.msg}
        return response

def db_add_transaction_to_user(data):
    db = get_db_connection()
    cursor = db.cursor()
    username = data['username']
    symbol = data['symbol']
    price = data['price']
    qty = data['quantity']
    ts = utils.parse_date(data['buy_timestamp'])
    try:
        cursor.execute("INSERT INTO stock_transaction (username, stock_symbol, price, quantity, buy_timestamp) VALUES (%s, %s, %s, %s, %s)", (username, symbol, float(price), float(qty), ts))
        db.commit()
        return {'status': 'Ok'}
    except mysql.connector.Error as err:
        return {'status': err.msg}
    
    except:
        return {'status': 'Error'}

def db_add_dividend_to_user(data):
    db = get_db_connection()
    cursor = db.cursor()
    username = data['username']
    symbol = data['symbol']
    value_euros = float(data['amount'])
    timestamp = utils.parse_date(data['timestamp'])
    try:
        cursor.execute('INSERT INTO dividend (received_on, username, stock_symbol, value_euros) VALUES (%s, %s, %s, %s)', (timestamp, username, symbol, value_euros))
        db.commit()
        logging.log(logging.INFO, 'Dividend added to user ' + username)
        return {'status': 'Ok'}
    except mysql.connector.Error as err:
        logging.log(logging.ERROR, err.msg)        
        return {'status': err.msg}

def db_get_user_transactions(data):
    data_list = utils.parse_endpoint_user_stock(data)
    username = data_list[0]
    symbol = data_list[1]
    db = get_db_connection()
    cursor = db.cursor()
    try:
        cursor.execute('SELECT username, stock_symbol, price, quantity, DATE_FORMAT(buy_timestamp, "%Y %M %d"), sold, sell_price, DATE_FORMAT(sell_timestamp, "%Y %M %d") FROM stock_transaction WHERE username = %s AND stock_symbol = %s', (username, symbol))
        transactions = cursor.fetchall()
        transactions = [list(transaction) for transaction in transactions]
        current_values = json.loads(financeData.get_value_data(symbol))
        current_values = [current_values[(list(current_values.keys())[-1])]]
        transactions = [current_values] + transactions
        cursor.execute('SELECT DATE_FORMAT(received_on, "%Y %M %d"), username, stock_symbol, value_euros FROM dividend WHERE username = %s AND stock_symbol = %s', (username, symbol))
        dividends = cursor.fetchall()
        dividends = [list(dividend) for dividend in dividends]
        transactions += dividends
        return transactions

    except mysql.connector.Error as err:
        return {'status': err.msg}

    except Exception as e:
        return {'status': str(e)}