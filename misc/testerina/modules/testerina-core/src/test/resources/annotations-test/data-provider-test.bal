import ballerina/test;
import ballerina/io;

@test:Config{
    dataProvider:"dataGen"
}
function testFunc1 (string fValue, string sValue, string result) returns error? {

    var value1 = check int.convert(fValue);
    var value2 = check int.convert(sValue);
    var result1 = check int.convert(result);
    io:println("Input params: ["+fValue+","+sValue+","+result+"]");
    test:assertEquals(value1 + value2, result1, msg = "The sum is not correct");
    return;
}

function dataGen() returns (string[][]) {
    return [["1", "2", "3"], ["10", "20", "30"], ["5", "6", "11"]];
}

@test:Config{
    dataProvider:"dataGen2"
}
function testFunc2 (string fValue, string sValue, string result) returns error? {

    var value1 = check int.convert(fValue);
    var value2 = check int.convert(sValue);
    var result1 = check int.convert(result);
    io:println("Input params: ["+fValue+","+sValue+","+result+"]");
    test:assertEquals(value1 + value2, result1, msg = "The sum is not correct");
    return;
}

@test:Config{
    dataProvider:"dataGen3"
}
function testFunc3 (json fValue, json sValue, json result) {
    json a = {"a": "a"};
    json b = {"b": "b"};
    json c = {"c": "c"};
    test:assertEquals(fValue, a, msg = "json data provider failed");
    test:assertEquals(sValue, b, msg = "json data provider failed");
    test:assertEquals(result, c, msg = "json data provider failed");
}

@test:Config{
    dataProvider:"dataGen4"
}
function testFunc4 (int aValue, int bValue, (int, int) result) {
    int a = 10;
    int b = 20;
    (int, int) c = (30, 30);
    test:assertEquals(aValue, a, msg = "tuple data provider failed");
    test:assertEquals(bValue, b, msg = "tuple data provider failed");
    test:assertEquals(result, c, msg = "tuple data provider failed");
}

function dataGen2() returns (string[][]) {
    return [["1", "2", "3"]];
}

function dataGen3() returns (json[][]) {
    return [[{"a": "a"}, {"b": "b"}, {"c": "c"}]];
}

function dataGen4() returns ((int, int, (int, int))[]) {
    return [(10, 20, (30, 30))];
}
