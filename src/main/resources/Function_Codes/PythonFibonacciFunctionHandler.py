import json
import time

def requestHandler(event, context):
    start_time = time.time()
    x = fibonacci(28)
    duration = time.time() - start_time
    return duration * 1000

def fibonacci(n):
    if n <= 1:
        return n
    return (fibonacci(n-1)+fibonacci(n-2))