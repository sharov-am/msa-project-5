FROM python:3.9-slim

RUN apt-get update && apt-get install -y libpq-dev gcc && rm -rf /var/lib/apt/lists/*
RUN pip install psycopg2-binary

WORKDIR /app
COPY export_script.py .

# Создаем папку для экспорта
RUN mkdir /data

CMD ["python", "export_script.py"]
