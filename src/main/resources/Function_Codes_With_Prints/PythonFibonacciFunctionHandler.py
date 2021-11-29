import time

def requestHandler(event, context):
    startTime = time.time()
    f1 = 0
    f2 = 1
    for i in range(100):
        print(f1)
        next = f1 + f2
        f1 = f2
        f2 = next
    return time.time() - startTime