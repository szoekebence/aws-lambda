exports.handler = async () => {
    const time = process.hrtime();
    var x = fibonacci(28);
    const diff = process.hrtime(time);
    return (diff[0] * 1000000000 + diff[1]) * 0.000001;
};

function fibonacci(n) {
    if (n <= 1) {
        return n;
    }
    return (fibonacci(n - 1) + fibonacci(n - 2));
}