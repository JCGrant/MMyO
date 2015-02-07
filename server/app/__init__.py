from flask import Flask
from flask_sqlalchemy import SQLAlchemy
from config import *

app = Flask(__name__)
app.config.from_object('app.config.Config')
db = SQLAlchemy(app)

import models, views
