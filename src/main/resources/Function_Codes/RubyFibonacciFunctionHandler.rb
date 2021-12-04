require 'json'
require 'benchmark'

def lambda_handler(event:, context:)
    time = Benchmark.realtime {
        f1 = 0
        f2 = 1
        10000.times do
            nextFib = f1 + f2
            f1 = f2
            f2 = nextFib
        end
    }
    { statusCode: 200, body: time * 1000 }
end