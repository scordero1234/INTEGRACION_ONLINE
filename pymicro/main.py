import json
from flask import Flask, request, jsonify,abort

app = Flask(__name__)

data = [
    {
        "codigo": 1,
        "identificacion": "0301645354",
        "name": "David Crespo",
        "age": 30,
        "email": "scorcar.cordero@gmail.com",
        "clientType": 1,
        "amount": 1.0
    }
]

@app.route('/clientes', methods=['GET'])
def get_clientes():
    if(request.method == 'GET'):
        return jsonify(data)
     


@app.route('/clientes/<int:codigo>', methods=['GET'])
def get_cliente(codigo):
    if(request.method == 'GET'):
        for cliente in data:
            if(cliente.get("codigo") == codigo):
                return jsonify(cliente)
            abort(404)
            
@app.route('/clientes', methods=['POST'])
def add_cliente():
    if((request.method =='POST') and (request.headers['Content-Type'] == 'application/json')):
        data.append(request.json)
        return jsonify({"message": "Cliente agregado correctamente"})
    else:
        abort(440,'Bad Request')
if __name__ == '__main__':
    app.run(debug=True)