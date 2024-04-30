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

@app.route('/users', methods=['POST'])
def create_user():
    data = request.get_json()
    db = get_db_connection()
    cursor = db.cursor()
    cursor.execute("INSERT INTO users (name, email) VALUES (%s, %s)", (data['name'], data['email']))
    db.commit()
    return jsonify({'id': cursor.lastrowid}), 201

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