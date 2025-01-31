import requests # type: ignore
from flask import Blueprint, render_template, jsonify

router = Blueprint('router', __name__)

@router.route('/')
def home():
    return render_template('index.html')


@router.route('/mapagrafos')
def mapagrafos():
    try:
        r = requests.get("http://localhost:8080/proyecto/habitaciones/mapadegrafos")
        if r.status_code == 200:
            graph_data = r.json()
            return render_template('grafos/grafos.html', graph_data=graph_data)
        else:
            return jsonify({"error": "No se pudo obtener el grafo"}), r.status_code
    except requests.exceptions.RequestException as e:
        print("Error de conexión: ", str(e))
        return jsonify({"error": "No se pudo conectar con el servidor"}), 502


@router.route('/adyacencias')
def adyacenciasaleatorias():
    r = requests.get("http://localhost:8080/proyecto/habitaciones/mostrarGrafos")
    print(r.status_code)  # Imprime el código de estado HTTP
    print(r.text)         # Imprime el contenido de la respuesta

    data = r.json()
    ## imprimirn en consola
    print(data)
    return render_template('grafos/adyacencias.html', lista=data)


@router.route('/adyacenciaAleatorea', methods=['GET'])
def adyacenciaAleatorea():
    try:
        r = requests.get("http://localhost:8080/proyecto/habitaciones/unionAleatoria")
        if r.status_code == 200:
            try:
                data = r.json()
                return jsonify(data)
            except ValueError:
                return jsonify({"error": "La respuesta no es JSON válida"}), 500
        else:
            return jsonify({"error": "No se pudo obtener la adyacencia"}), r.status_code
    except requests.exceptions.RequestException as e:
        print("Error de conexión: ", str(e))
        return jsonify({"error": "No se pudo conectar con el servidor"}), 502


@router.route('/caminoCorto/<int:origen>/<int:destino>/<int:algoritmo>', methods=['GET'])
def caminoCorto(origen, destino, algoritmo):
    try:
        url = f"http://localhost:8080/proyecto/habitaciones/caminoCorto/{origen}/{destino}/{algoritmo}"
        r = requests.get(url)
        if r.status_code == 200:
            try:
                data = r.json()
                return jsonify(data)
            except ValueError:
                return jsonify({"error": "La respuesta no es JSON válida"}), 500
        else:
            return jsonify({"error": "No se pudo calcular el camino corto"}), r.status_code
    except requests.exceptions.RequestException as e:
        print("Error de conexión:", str(e))
        return jsonify({"error": "No se pudo conectar con el servidor de cálculo"}), 502


@router.route('/camino', methods=['GET'])
def mostrarGrafos():
    try:
        r = requests.get("http://localhost:8080/proyecto/habitaciones/mostrarGrafos")
        if r.status_code == 200:
            try:
                data = r.json()
                return render_template('grafos/camino.html', data=data)
            except ValueError:
                return jsonify({"error": "La respuesta no es JSON válida"}), 500
        else:
            return jsonify({"error": "No se pudo obtener los datos"}), r.status_code
    except requests.exceptions.RequestException as e:
        print("Error de conexión:", str(e))
        return jsonify({"error": "No se pudo conectar con el servidor"}), 502

@router.route('/bfs/<int:origen>', methods=['GET'])
def busquedaAnchura(origen):
    try:
        # Realizar la solicitud al backend de Java para ejecutar BFS
        url = f"http://localhost:8080/proyecto/habitaciones/bfs/{origen}"
        r = requests.get(url)

        if r.status_code == 200:
            # Verificar si la respuesta tiene contenido
            if r.text.strip():
                # Intentar convertir la respuesta a JSON
                data = r.json()
                return jsonify(data)  # Retornar la respuesta como JSON para procesar en el frontend
            else:
                return jsonify({"error": "La respuesta está vacía"}), 500
        else:
            return jsonify({"error": f"Error al ejecutar BFS. Código de estado: {r.status_code}"}), r.status_code

    except requests.exceptions.RequestException as e:
        print("Error de conexión:", str(e))
        return jsonify({"error": "No se pudo conectar con el servidor de BFS"}), 500

