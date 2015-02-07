import os

base_dir = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))

class Config:
    DEBUG = True
    SQLALCHEMY_DATABASE_URI = 'sqlite:///' + os.path.join(base_dir, 'app.db')
