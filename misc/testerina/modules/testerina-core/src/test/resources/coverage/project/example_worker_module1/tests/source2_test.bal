import ballerina/test;
//import ballerina/io;

//function beforeFunc2() {
//    io:println("I'm the before function2!");
//}

@test:Config {
    //before: "beforeFunc2",
    //after: "afterFunc2"
}
function testFunctionWorker() {
    sourceFuncWorker();
}

//function afterFunc2() {
//    io:println("I'm the after function2!");
//}
