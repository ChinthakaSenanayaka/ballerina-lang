package ballerina.net.httpsession;

import ballerina.doc;

struct Session {
    string id;
}

@doc:Description { value:"Gets the session struct"}
@doc:Param { value:"m: A message object" }
@doc:Return { value:"Session: HTTP session struct" }
native function getSession (message m) (Session);

@doc:Description { value:"Gets the session struct"}
@doc:Param { value:"m: A message object" }
@doc:Param { value:"create: Create a new session or not" }
@doc:Return { value:"Session: HTTP session struct" }
native function getSessionWithParam (message m, boolean create1) (Session);

@doc:Description { value:"Gets the session attribute"}
@doc:Param { value:"session: A session struct" }
@doc:Param { value:"attributeKey: HTTPSession attribute key" }
@doc:Return { value:"any: HTTPSession attribute value" }
native function getAttribute (Session session, string attributeKey) (any);

@doc:Description { value:"Sets session attributes to the message"}
@doc:Param { value:"session: A session struct" }
@doc:Param { value:"attributeKey: HTTPSession attribute key" }
@doc:Param { value:"attributeValue: HTTPSession attribute Value" }
native function setAttribute (Session session, string attributeKey, any attributeValue);

@doc:Description { value:"Gets the session attribute names"}
@doc:Param { value:"session: A session struct" }
@doc:Return { value:"string[]: HTTPSession attribute name array" }
native function getAttributeNames (Session session) (string[]);

@doc:Description { value:"Gets the session attribute"}
@doc:Param { value:"session: A session struct" }
native function invalidate (Session session);

@doc:Description { value:"Remove the session attribute"}
@doc:Param { value:"session: A session struct" }
@doc:Param { value:"attributeKey: HTTPSession attribute key" }
native function removeAttribute (Session session, string attributeKey);




