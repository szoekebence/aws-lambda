exports.handler = async () => {
    const time = process.hrtime();
    var f1 = 0;
    var f2 = 1;
    for (var i = 1; i < 100; i++) {
        console.log(f1);
        var next = f1 + f2;
        f1 = f2;
        f2 = next;
    }
    const diff = process.hrtime(time);
    return (diff[0] * 1000000000 + diff[1]) * 0.000001;
};
