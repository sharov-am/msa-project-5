import json
import os
from datetime import datetime, timedelta
from airflow import DAG
from airflow.operators.python import PythonOperator, BranchPythonOperator
from airflow.operators.empty import EmptyOperator


DATA_FILE = '/opt/airflow/data/input.json'

def notify_failure(context):
    print(f"!!! ALERT !!! Task {context['task_instance'].task_id} failed.")

def notify_success(context):
    print(f"--- SUCCESS --- Pipeline finished at {context['end_date']}")

default_args = {
    'owner': 'airflow',
    'retries': 2,
    'retry_delay': timedelta(seconds=30),
    'on_failure_callback': notify_failure,
    'on_success_callback': notify_success,
}

with DAG(
    'file_based_poc',
    default_args=default_args,
    start_date=datetime(2023, 1, 1),
    schedule_interval=None,
    catchup=False
) as dag:

    def read_from_file():
        if not os.path.exists(DATA_FILE):
            raise FileNotFoundError(f"Файл {DATA_FILE} не найден!")
        
        with open(DATA_FILE, 'r') as f:
            data = json.load(f)
        
        count = data.get('records_count', 0)
        print(f"Найдено записей в файле: {count}")
        return count

    extract_task = PythonOperator(
        task_id='read_file_source',
        python_callable=read_from_file
    )

    def branch_func(ti):
        count = ti.xcom_pull(task_ids='read_file_source')
        if count > 1000000:
            return 'big_data_path'
        return 'small_data_path'

    branch_task = BranchPythonOperator(
        task_id='check_volume',
        python_callable=branch_func
    )

    big_data_path = EmptyOperator(task_id='big_data_path')
    small_data_path = EmptyOperator(task_id='small_data_path')

    final_task = EmptyOperator(
        task_id='done',
        trigger_rule='none_failed_min_one_success'
    )

    extract_task >> branch_task >> [big_data_path, small_data_path] >> final_task
