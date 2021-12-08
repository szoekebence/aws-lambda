require 'json'
require 'benchmark'

def lambda_handler(event:, context:)
    time = Benchmark.realtime {
        x = fibonacci(28)
    }
    { statusCode: 200, body: time * 1000 }
end

def fibonacci(n)
    if n <= 1
        return n
    end
    (fibonacci(n-1)+fibonacci(n-2))
end