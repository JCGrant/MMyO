from app import app
import json

@app.route("/<pose>/<direction>")
def spell(pose, direction):
    print pose, direction
    return json.dumps({
        "pose": pose,
        "direction": direction
    })
