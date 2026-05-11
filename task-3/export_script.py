import os
import csv
import psycopg2
from datetime import datetime

def export_table():
    # Получаем настройки из ENV
    db_uri = os.getenv("DATABASE_URL", "postgresql://user:pass@localhost:5432/db")
    table_name = os.getenv("TABLE_NAME", "my_table")
    output_path = f"/data/export_{table_name}_{datetime.now().strftime('%Y%m%d_%H%M%S')}.csv"

    try:
        conn = psycopg2.connect(db_uri)
        cursor = conn.cursor()
        
        with open(output_path, 'w', newline='') as f:
            cursor.copy_expert(f"COPY {table_name} TO STDOUT WITH CSV HEADER", f)
            
        print(f"Успешный экспорт {table_name} в {output_path}")
    except Exception as e:
        print(f"Ошибка: {e}")
        exit(1)
    finally:
        if 'conn' in locals(): conn.close()

if __name__ == "__main__":
    export_table()
