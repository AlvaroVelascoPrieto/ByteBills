from flask import Flask, request, jsonify
import mysql.connector
import financeData as fd
import db_interaction

app = Flask(__name__)

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

@app.route('/register', methods=['POST'])
def create_user():
    data = request.get_json()
    db = get_db_connection()
    cursor = db.cursor()
    try:
        cursor.execute("INSERT INTO users (username, email, password) VALUES (%s, %s, %s)", (data['username'], data['email'], data['password']))
        db.commit()
        response = jsonify({'status': 'Ok'})
        response.headers['Content-Length'] = str(len(response.get_data()))
        return response, 200
    except mysql.connector.Error as err:
        response = jsonify({'status': err.msg})
        response.headers['Content-Length'] = str(len(response.get_data()))
        return response, 200

@app.route('/login', methods=['POST'])
def login_user():
    data = request.get_json()
    db = get_db_connection()
    cursor = db.cursor()
    cursor.execute("SELECT * FROM users WHERE username=%s AND password=%s", (data['username'], data['password']))
    user = cursor.fetchone()
    if user:
        response = jsonify({'status': 'Ok'})
        response.headers['Content-Length'] = str(len(response.get_data()))
        return response, 200
    else:
        response = jsonify({'status': 'Not Found'})
        response.headers['Content-Length'] = str(len(response.get_data()))
        return response, 200

##################
## STOCK ROUTES ##
##################

@app.route('/user-stocks/<username>', methods=['GET'])
def get_user_tracked_stocks(username):
    db = get_db_connection()
    cursor = db.cursor()
    try:
        cursor.execute("SELECT symbol, name FROM stocks s join stock_user su on s.symbol = su.stock_symbol WHERE su.username=%s", (username,))
        stocks = cursor.fetchall()
        response = jsonify(stocks)
        response.headers['Content-Length'] = str(len(response.get_data()))
        return response, 200
    except mysql.connector.Error as err:
        response = jsonify({'status': err.msg})
        response.headers['Content-Length'] = str(len(response.get_data()))
        return response, 200

@app.route('/add-stock-to-user', methods=['POST'])
def add_symbol_to_user():

    print("DATA ========== \n\n\n\n")
    print(request.get_json())
    print("\n\n\n DATA ===========")
    data = request.get_json()
    print("DATA ========== \n\n\n\n")
    print(data)
    print("\n\n\n\n DATA")
    response = db_interaction.db_add_stock_to_user(data)
    response = jsonify(response)
    response.headers['Content-Length'] = str(len(response.get_data()))
    return response, 200

@app.route('/user-transactions/<username>', methods=['GET'])
def get_user_stocks(username):
    db = get_db_connection()
    cursor = db.cursor()
    try:
        cursor.execute("SELECT * FROM stock_transaction WHERE username = %s", (username))
        stocks = cursor.fetchall()
        response = jsonify(stocks)
        response.headers['Content-Length'] = str(len(response.get_data()))
        return response, 200
    except mysql.connector.Error as err:
        response = jsonify({'status': err.msg})
        response.headers['Content-Length'] = str(len(response.get_data()))
        return response, 200

@app.route('/add-transaction-to-user', methods=['POST'])
def add_transaction_to_user():
    data = request.get_json()
    db = get_db_connection()
    cursor = db.cursor()
    try:
        cursor.execute("INSERT INTO stock_transaction (username, stock_symbol, price, quantity, buy_timestamp, sold) VALUES (%s, %s, %f, %f, %ld, %b)", (data['username'], data['symbol'], data['price'], data['quantity'], data['buy_timestamp'], False))
        db.commit()
        response = jsonify({'status': 'Ok'})
        response.headers['Content-Length'] = str(len(response.get_data()))
        return response, 200
    except mysql.connector.Error as err:
        response = jsonify({'status': err.msg})
        response.headers['Content-Length'] = str(len(response.get_data()))
        return response, 200

@app.route('/users/<int:user_id>', methods=['GET'])
def get_user(user_id):
    db = get_db_connection()
    cursor = db.cursor()
    cursor.execute("SELECT * FROM users WHERE id=%s", (user_id,))
    user = cursor.fetchone()
    return jsonify({'id': user[0], 'name': user[1], 'email': user[2]})

@app.route('/users/<int:user_id>', methods=['PUT'])
def update_user(user_id):
    data = request.get_json()
    db = get_db_connection()
    cursor = db.cursor()
    cursor.execute("UPDATE users SET name=%s, email=%s WHERE id=%s", (data['name'], data['email'], user_id))
    db.commit()
    return jsonify({'id': user_id, 'name': data['name'], 'email': data['email']})

@app.route('/users/<int:user_id>', methods=['DELETE'])
def delete_user(user_id):
    db = get_db_connection()
    cursor = db.cursor()
    cursor.execute("DELETE FROM users WHERE id=%s", (user_id,))
    db.commit()
    return '', 204

@app.route('/stock/<stock_id>', methods=['GET'])
def get_stock(stock_id):
    res = fd.get_value_data(stock_id)
    return jsonify(res)

@app.route('/stock', methods=['GET'])
def get_stock_symbols():
    res = fd.get_stock_symbols()
    return jsonify(res)

@app.route('/crypto', methods=['GET'])
def get_crypto_symbols():
    res = fd.get_crypto_symbols()
    return jsonify(res)

@app.route('/', methods=['GET'])
def hello():
    return jsonify({'message': 'Hello World'})
