from app import db
from werkzeug.security import generate_password_hash, \
    check_password_hash

spells = db.Table('spells',
    db.Column('spell_id', db.Integer, db.ForeignKey('spell.id')),
    db.Column('player_id', db.Integer, db.ForeignKey('player.id'))
)

class Player(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    character_name = db.Column(db.String(20), nullable=False)
    email = db.Column(db.String(120), unique=True)
    pw_hash = db.Column(db.String(64), nullable=False)
    spells = db.relationship('Spell', secondary=spells,
        backref=db.backref('players', lazy='dynamic'))

    def __init__(self, character_name, email, password):
        self.character_name = character_name,
        self.email = email
        self.set_password(password)
    
    def set_password(self, password):
        self.pw_hash = generate_password_hash(password)

    def check_password(self, password):
        return check_password_hash(self.pw_hash, password)

class Spell(db.Model):
    id = db.Column(db.Integer, primary_key=True)

