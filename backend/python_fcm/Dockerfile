FROM python:3.11.6-slim-bullseye

COPY . /app

WORKDIR /app

RUN pip install -r requirements.txt

CMD ["python", "-u", "fcm_messaging.py"]