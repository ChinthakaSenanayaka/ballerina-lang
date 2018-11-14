import ballerina/test;
import ballerina/io;
import ballerina/http;
import ballerina/log;

function beforeFuncService() {
    io:println("I'm the before function service test!");
}

endpoint http:Client clientEP {
    url:"http://localhost:9090"
};

@test:Config {
    before: "beforeFuncService",
    after: "afterFuncService"
}
function testFunctionService() {
    io:println("I'm in test function  service test!");

    var resp = clientEP->get("/hello/sayHello");

    match resp {
        http:Response response => {

            match (response.getTextPayload()) {

                string res => test:assertEquals(res, "Hello, World!", msg = "Failed  service test!");

                error err => log:printError(err.message);
            }
        }

        error err => log:printError(err.message);
    }

}

function afterFuncService() {
    io:println("I'm the after function  service test!");
}
