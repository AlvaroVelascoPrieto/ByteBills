from flask import Flask, request, jsonify
import mysql.connector

app = Flask(__name__)

def get_db_connection():
    return mysql.connector.connect(
        host="db",
        user="root",
        password="root",
        database="bytebills"
    )

@app.route('/register', methods=['POST'])
def create_user():
    data = request.get_json()
    db = get_db_connection()
    cursor = db.cursor()
    cursor.execute("INSERT INTO users (name, email, password) VALUES (%s, %s, %s)", (data['username'], data['email'], data['password']))
    try:
        db.commit()
        return jsonify({'status': 'Ok'}), 200
    except:
        return jsonify({'status': 'Error'}), 200

@app.route('/login', methods=['POST'])
def login_user():
    data = request.get_json()
    db = get_db_connection()
    cursor = db.cursor()
    cursor.execute("SELECT * FROM users WHERE email=%s AND password=%s", (data['email'], data['password']))
    user = cursor.fetchone()
    if user:
        return jsonify({'status': 'Ok'}), 200
    else:
        return jsonify({'status': 'Error'}), 200

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

if __name__ == '__main__':
    app.run(debug=True)