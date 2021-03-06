type Person record {
    string name;
    int age;
    string address;
    !...
};

type Student record {
    string name;
    int age;
    string address;
    string class;
    !...
};

function testJsonInitializationWithStructConstraintInvalid() returns (json, json, json){
    json<Person> j = {firstName:"John Doe", age:5, address:"London"};
    return (j.name, j.age, j.address);
}

function testInvalidStructFieldConstraintLhs() returns (json){
    json<Person> j = {};
    j.firstName = "Ann";
    return j;
}

function tesInvalidStructFieldConstraintRhs() returns (json){
    json<Person> j = {};
    j.name = "Ann";
    json name = j.firstName;
    return name;
}

function testConstraintJSONIndexing() returns (json){
    json<Student> j = {name:"John Doe", age:30, address:"Colombo", class:"5"};
    return j["bus"];
}

type Employee record {
    string first_name;
    string last_name;
    int age;
    Address address;
    !...
};

type Address record {
    string number;
    string street;
    string city;
    PhoneNumber phoneNumber;
    !...
};

type PhoneNumber record {
    string areaCode;
    string number;
    !...
};

function tesInvalidNestedStructFieldAccess() {
    json<Employee> e = {first_name: "John",last_name: "Doe",age: 30,address: {phoneNumber: {number:"456"}, street:"York St"}};
    json j = e.address.phoneNumber.foo;
}

function tesInvalidNestedStructFieldIndexAccess() {
    json<Employee> e = {first_name: "John",last_name: "Doe",age: 30,address: {phoneNumber: {number:"456"}, street:"York St"}};
    json j = e["address"]["phoneNumber"]["bar"];
}

function tesInitializationWithInvalidNestedStruct() {
    json<Employee> e = {first_name: "John",last_name: "Doe",age: 30,address: {phoneNumber: {number:"456", foo:5}, street:"York St"}};
}

function testConstrainedJSONArrayToConstraintJsonArrayCast() returns (json<Student>[]) {
    json<Person>[] j1 = [{name:"John Doe", age:30, address:"London"}];
    var j2 = json<Student>[].convert(j1);
    return j2;
}

function testBooleanArrayToJsonAssignment() returns (json) {
    byte[] b = [];
    json j = b;
    return j;
}

function testInvalidIndexBasedAccessOfRecordConstrainedJSON() {
    json<Person> j = {name:"John Doe", age:30, address:"London"};
    string name = <string>j[0];
}

function testInvalidIndexBasedAccessOfConstrainedJSON() {
    json j = 12;
    string name = j[0.0];
}

function getStudent() returns (json<Student>){
    json<Student> j = {name:"John Doe", age:30, address:"Colombo", class:"5"};
    return j;
}

function testConstraintJSONToConstraintJsonCast() returns (json) {
    json<Person> j = getStudent();
    return j;
}

function testConstraintJSONToConstraintJsonUnsafePositiveCast() returns (json | error) {
    json<Person> jp = getStudent();
    var js  = json<Student>.convert(jp);
    return js;
}

function testConstraintJSONToConstraintJsonUnsafeNegativeCast() returns (json | error) {
    json<Employee> je = {first_name:"John", last_name:"Doe", age:30, address:{phoneNumber:{number:"1234"},
                        street:"York St"}};
    var js = json<Student>.convert(je);
    return js;
}

