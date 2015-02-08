from app import app
import json

@app.route("/<int:user_id>/<pose>/<direction>")
def cast_spell(user_id, pose, direction):
    print user_id, pose, direction
    return json.dumps({
        "id": user_id,
        "pose": pose,
        "direction": direction
    })

@app.route("/<int:user_id>/<float:longitude>/<float:latitude>")
def send_location(user_id, longitude, latitude):
    print user_id, longitude, latitude
    return json.dumps({
        "id": user_id,
        "longitude": longitude,
        "latitude": latitude
    })
