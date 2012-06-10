//
// Definitions for schema: http://services.resources.remoting.xdocreport.opensagres.fr/
//  file:/Users/pascalleclercq/git/xdocreportGoogle/remoting/fr.opensagres.xdocreport.remoting.resources/src/main/wsdl/JAXWSResourcesServiceService.wsdl#types2
//
//
// Definitions for schema: null
//  file:/Users/pascalleclercq/git/xdocreportGoogle/remoting/fr.opensagres.xdocreport.remoting.resources/src/main/wsdl/JAXWSResourcesServiceService.wsdl#types1
//
//
// Definitions for schema: http://services.resources.remoting.xdocreport.opensagres.fr/
//  file:/Users/pascalleclercq/git/xdocreportGoogle/remoting/fr.opensagres.xdocreport.remoting.resources/src/main/wsdl/JAXWSResourcesServiceService_schema1.xsd
//
//
// Constructor for XML Schema item {http://services.resources.remoting.xdocreport.opensagres.fr/}resource
//
function services_resources_remoting_xdocreport_opensagres_fr__resource () {
    this.typeMarker = 'services_resources_remoting_xdocreport_opensagres_fr__resource';
    this._children = [];
    this._id = null;
    this._name = null;
    this._type = null;
}

//
// accessor is services_resources_remoting_xdocreport_opensagres_fr__resource.prototype.getChildren
// element get for children
// - element type is {http://services.resources.remoting.xdocreport.opensagres.fr/}resource
// - required element
// - array
// - nillable
//
// element set for children
// setter function is is services_resources_remoting_xdocreport_opensagres_fr__resource.prototype.setChildren
//
function services_resources_remoting_xdocreport_opensagres_fr__resource_getChildren() { return this._children;}

services_resources_remoting_xdocreport_opensagres_fr__resource.prototype.getChildren = services_resources_remoting_xdocreport_opensagres_fr__resource_getChildren;

function services_resources_remoting_xdocreport_opensagres_fr__resource_setChildren(value) { this._children = value;}

services_resources_remoting_xdocreport_opensagres_fr__resource.prototype.setChildren = services_resources_remoting_xdocreport_opensagres_fr__resource_setChildren;
//
// accessor is services_resources_remoting_xdocreport_opensagres_fr__resource.prototype.getId
// element get for id
// - element type is {http://www.w3.org/2001/XMLSchema}string
// - optional element
//
// element set for id
// setter function is is services_resources_remoting_xdocreport_opensagres_fr__resource.prototype.setId
//
function services_resources_remoting_xdocreport_opensagres_fr__resource_getId() { return this._id;}

services_resources_remoting_xdocreport_opensagres_fr__resource.prototype.getId = services_resources_remoting_xdocreport_opensagres_fr__resource_getId;

function services_resources_remoting_xdocreport_opensagres_fr__resource_setId(value) { this._id = value;}

services_resources_remoting_xdocreport_opensagres_fr__resource.prototype.setId = services_resources_remoting_xdocreport_opensagres_fr__resource_setId;
//
// accessor is services_resources_remoting_xdocreport_opensagres_fr__resource.prototype.getName
// element get for name
// - element type is {http://www.w3.org/2001/XMLSchema}string
// - optional element
//
// element set for name
// setter function is is services_resources_remoting_xdocreport_opensagres_fr__resource.prototype.setName
//
function services_resources_remoting_xdocreport_opensagres_fr__resource_getName() { return this._name;}

services_resources_remoting_xdocreport_opensagres_fr__resource.prototype.getName = services_resources_remoting_xdocreport_opensagres_fr__resource_getName;

function services_resources_remoting_xdocreport_opensagres_fr__resource_setName(value) { this._name = value;}

services_resources_remoting_xdocreport_opensagres_fr__resource.prototype.setName = services_resources_remoting_xdocreport_opensagres_fr__resource_setName;
//
// accessor is services_resources_remoting_xdocreport_opensagres_fr__resource.prototype.getType
// element get for type
// - element type is {http://services.resources.remoting.xdocreport.opensagres.fr/}resourceType
// - optional element
//
// element set for type
// setter function is is services_resources_remoting_xdocreport_opensagres_fr__resource.prototype.setType
//
function services_resources_remoting_xdocreport_opensagres_fr__resource_getType() { return this._type;}

services_resources_remoting_xdocreport_opensagres_fr__resource.prototype.getType = services_resources_remoting_xdocreport_opensagres_fr__resource_getType;

function services_resources_remoting_xdocreport_opensagres_fr__resource_setType(value) { this._type = value;}

services_resources_remoting_xdocreport_opensagres_fr__resource.prototype.setType = services_resources_remoting_xdocreport_opensagres_fr__resource_setType;
//
// Serialize {http://services.resources.remoting.xdocreport.opensagres.fr/}resource
//
function services_resources_remoting_xdocreport_opensagres_fr__resource_serialize(cxfjsutils, elementName, extraNamespaces) {
    var xml = '';
    if (elementName != null) {
     xml = xml + '<';
     xml = xml + elementName;
     if (extraNamespaces) {
      xml = xml + ' ' + extraNamespaces;
     }
     xml = xml + '>';
    }
    // block for local variables
    {
     if (this._children != null) {
      for (var ax = 0;ax < this._children.length;ax ++) {
       if (this._children[ax] == null) {
        xml = xml + '<children xsi:nil=\'true\'/>';
       } else {
        xml = xml + this._children[ax].serialize(cxfjsutils, 'children', null);
       }
      }
     }
    }
    // block for local variables
    {
     if (this._id != null) {
      xml = xml + '<id>';
      xml = xml + cxfjsutils.escapeXmlEntities(this._id);
      xml = xml + '</id>';
     }
    }
    // block for local variables
    {
     if (this._name != null) {
      xml = xml + '<name>';
      xml = xml + cxfjsutils.escapeXmlEntities(this._name);
      xml = xml + '</name>';
     }
    }
    // block for local variables
    {
     if (this._type != null) {
      xml = xml + '<type>';
      xml = xml + cxfjsutils.escapeXmlEntities(this._type);
      xml = xml + '</type>';
     }
    }
    if (elementName != null) {
     xml = xml + '</';
     xml = xml + elementName;
     xml = xml + '>';
    }
    return xml;
}

services_resources_remoting_xdocreport_opensagres_fr__resource.prototype.serialize = services_resources_remoting_xdocreport_opensagres_fr__resource_serialize;

function services_resources_remoting_xdocreport_opensagres_fr__resource_deserialize (cxfjsutils, element) {
    var newobject = new services_resources_remoting_xdocreport_opensagres_fr__resource();
    cxfjsutils.trace('element: ' + cxfjsutils.traceElementName(element));
    var curElement = cxfjsutils.getFirstElementChild(element);
    var item;
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing children');
    if (curElement != null && cxfjsutils.isNodeNamedNS(curElement, '', 'children')) {
     item = [];
     do  {
      var arrayItem;
      var value = null;
      if (!cxfjsutils.isElementNil(curElement)) {
       arrayItem = services_resources_remoting_xdocreport_opensagres_fr__resource_deserialize(cxfjsutils, curElement);
      }
      item.push(arrayItem);
      curElement = cxfjsutils.getNextElementSibling(curElement);
     }
       while(curElement != null && cxfjsutils.isNodeNamedNS(curElement, '', 'children'));
     newobject.setChildren(item);
     var item = null;
    }
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing id');
    if (curElement != null && cxfjsutils.isNodeNamedNS(curElement, '', 'id')) {
     var value = null;
     if (!cxfjsutils.isElementNil(curElement)) {
      value = cxfjsutils.getNodeText(curElement);
      item = value;
     }
     newobject.setId(item);
     var item = null;
     if (curElement != null) {
      curElement = cxfjsutils.getNextElementSibling(curElement);
     }
    }
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing name');
    if (curElement != null && cxfjsutils.isNodeNamedNS(curElement, '', 'name')) {
     var value = null;
     if (!cxfjsutils.isElementNil(curElement)) {
      value = cxfjsutils.getNodeText(curElement);
      item = value;
     }
     newobject.setName(item);
     var item = null;
     if (curElement != null) {
      curElement = cxfjsutils.getNextElementSibling(curElement);
     }
    }
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing type');
    if (curElement != null && cxfjsutils.isNodeNamedNS(curElement, '', 'type')) {
     var value = null;
     if (!cxfjsutils.isElementNil(curElement)) {
      value = cxfjsutils.getNodeText(curElement);
      item = value;
     }
     newobject.setType(item);
     var item = null;
     if (curElement != null) {
      curElement = cxfjsutils.getNextElementSibling(curElement);
     }
    }
    return newobject;
}

//
// Constructor for XML Schema item {http://services.resources.remoting.xdocreport.opensagres.fr/}propertyRepresentation
//
function services_resources_remoting_xdocreport_opensagres_fr__propertyRepresentation () {
    this.typeMarker = 'services_resources_remoting_xdocreport_opensagres_fr__propertyRepresentation';
    this._name = null;
    this._value = null;
}

//
// accessor is services_resources_remoting_xdocreport_opensagres_fr__propertyRepresentation.prototype.getName
// element get for name
// - element type is {http://www.w3.org/2001/XMLSchema}string
// - optional element
//
// element set for name
// setter function is is services_resources_remoting_xdocreport_opensagres_fr__propertyRepresentation.prototype.setName
//
function services_resources_remoting_xdocreport_opensagres_fr__propertyRepresentation_getName() { return this._name;}

services_resources_remoting_xdocreport_opensagres_fr__propertyRepresentation.prototype.getName = services_resources_remoting_xdocreport_opensagres_fr__propertyRepresentation_getName;

function services_resources_remoting_xdocreport_opensagres_fr__propertyRepresentation_setName(value) { this._name = value;}

services_resources_remoting_xdocreport_opensagres_fr__propertyRepresentation.prototype.setName = services_resources_remoting_xdocreport_opensagres_fr__propertyRepresentation_setName;
//
// accessor is services_resources_remoting_xdocreport_opensagres_fr__propertyRepresentation.prototype.getValue
// element get for value
// - element type is {http://www.w3.org/2001/XMLSchema}string
// - optional element
//
// element set for value
// setter function is is services_resources_remoting_xdocreport_opensagres_fr__propertyRepresentation.prototype.setValue
//
function services_resources_remoting_xdocreport_opensagres_fr__propertyRepresentation_getValue() { return this._value;}

services_resources_remoting_xdocreport_opensagres_fr__propertyRepresentation.prototype.getValue = services_resources_remoting_xdocreport_opensagres_fr__propertyRepresentation_getValue;

function services_resources_remoting_xdocreport_opensagres_fr__propertyRepresentation_setValue(value) { this._value = value;}

services_resources_remoting_xdocreport_opensagres_fr__propertyRepresentation.prototype.setValue = services_resources_remoting_xdocreport_opensagres_fr__propertyRepresentation_setValue;
//
// Serialize {http://services.resources.remoting.xdocreport.opensagres.fr/}propertyRepresentation
//
function services_resources_remoting_xdocreport_opensagres_fr__propertyRepresentation_serialize(cxfjsutils, elementName, extraNamespaces) {
    var xml = '';
    if (elementName != null) {
     xml = xml + '<';
     xml = xml + elementName;
     if (extraNamespaces) {
      xml = xml + ' ' + extraNamespaces;
     }
     xml = xml + '>';
    }
    // block for local variables
    {
     if (this._name != null) {
      xml = xml + '<name>';
      xml = xml + cxfjsutils.escapeXmlEntities(this._name);
      xml = xml + '</name>';
     }
    }
    // block for local variables
    {
     if (this._value != null) {
      xml = xml + '<value>';
      xml = xml + cxfjsutils.escapeXmlEntities(this._value);
      xml = xml + '</value>';
     }
    }
    if (elementName != null) {
     xml = xml + '</';
     xml = xml + elementName;
     xml = xml + '>';
    }
    return xml;
}

services_resources_remoting_xdocreport_opensagres_fr__propertyRepresentation.prototype.serialize = services_resources_remoting_xdocreport_opensagres_fr__propertyRepresentation_serialize;

function services_resources_remoting_xdocreport_opensagres_fr__propertyRepresentation_deserialize (cxfjsutils, element) {
    var newobject = new services_resources_remoting_xdocreport_opensagres_fr__propertyRepresentation();
    cxfjsutils.trace('element: ' + cxfjsutils.traceElementName(element));
    var curElement = cxfjsutils.getFirstElementChild(element);
    var item;
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing name');
    if (curElement != null && cxfjsutils.isNodeNamedNS(curElement, '', 'name')) {
     var value = null;
     if (!cxfjsutils.isElementNil(curElement)) {
      value = cxfjsutils.getNodeText(curElement);
      item = value;
     }
     newobject.setName(item);
     var item = null;
     if (curElement != null) {
      curElement = cxfjsutils.getNextElementSibling(curElement);
     }
    }
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing value');
    if (curElement != null && cxfjsutils.isNodeNamedNS(curElement, '', 'value')) {
     var value = null;
     if (!cxfjsutils.isElementNil(curElement)) {
      value = cxfjsutils.getNodeText(curElement);
      item = value;
     }
     newobject.setValue(item);
     var item = null;
     if (curElement != null) {
      curElement = cxfjsutils.getNextElementSibling(curElement);
     }
    }
    return newobject;
}

//
// Constructor for XML Schema item {http://services.resources.remoting.xdocreport.opensagres.fr/}binaryData
//
function services_resources_remoting_xdocreport_opensagres_fr__binaryData () {
    this.typeMarker = 'services_resources_remoting_xdocreport_opensagres_fr__binaryData';
    this._content = null;
    this._fileName = null;
    this._length = 0;
    this._mimeType = null;
    this._resourceId = null;
}

//
// accessor is services_resources_remoting_xdocreport_opensagres_fr__binaryData.prototype.getContent
// element get for content
// - element type is {http://www.w3.org/2001/XMLSchema}base64Binary
// - optional element
//
// element set for content
// setter function is is services_resources_remoting_xdocreport_opensagres_fr__binaryData.prototype.setContent
//
function services_resources_remoting_xdocreport_opensagres_fr__binaryData_getContent() { return this._content;}

services_resources_remoting_xdocreport_opensagres_fr__binaryData.prototype.getContent = services_resources_remoting_xdocreport_opensagres_fr__binaryData_getContent;

function services_resources_remoting_xdocreport_opensagres_fr__binaryData_setContent(value) { this._content = value;}

services_resources_remoting_xdocreport_opensagres_fr__binaryData.prototype.setContent = services_resources_remoting_xdocreport_opensagres_fr__binaryData_setContent;
//
// accessor is services_resources_remoting_xdocreport_opensagres_fr__binaryData.prototype.getFileName
// element get for fileName
// - element type is {http://www.w3.org/2001/XMLSchema}string
// - optional element
//
// element set for fileName
// setter function is is services_resources_remoting_xdocreport_opensagres_fr__binaryData.prototype.setFileName
//
function services_resources_remoting_xdocreport_opensagres_fr__binaryData_getFileName() { return this._fileName;}

services_resources_remoting_xdocreport_opensagres_fr__binaryData.prototype.getFileName = services_resources_remoting_xdocreport_opensagres_fr__binaryData_getFileName;

function services_resources_remoting_xdocreport_opensagres_fr__binaryData_setFileName(value) { this._fileName = value;}

services_resources_remoting_xdocreport_opensagres_fr__binaryData.prototype.setFileName = services_resources_remoting_xdocreport_opensagres_fr__binaryData_setFileName;
//
// accessor is services_resources_remoting_xdocreport_opensagres_fr__binaryData.prototype.getLength
// element get for length
// - element type is {http://www.w3.org/2001/XMLSchema}long
// - required element
//
// element set for length
// setter function is is services_resources_remoting_xdocreport_opensagres_fr__binaryData.prototype.setLength
//
function services_resources_remoting_xdocreport_opensagres_fr__binaryData_getLength() { return this._length;}

services_resources_remoting_xdocreport_opensagres_fr__binaryData.prototype.getLength = services_resources_remoting_xdocreport_opensagres_fr__binaryData_getLength;

function services_resources_remoting_xdocreport_opensagres_fr__binaryData_setLength(value) { this._length = value;}

services_resources_remoting_xdocreport_opensagres_fr__binaryData.prototype.setLength = services_resources_remoting_xdocreport_opensagres_fr__binaryData_setLength;
//
// accessor is services_resources_remoting_xdocreport_opensagres_fr__binaryData.prototype.getMimeType
// element get for mimeType
// - element type is {http://www.w3.org/2001/XMLSchema}string
// - optional element
//
// element set for mimeType
// setter function is is services_resources_remoting_xdocreport_opensagres_fr__binaryData.prototype.setMimeType
//
function services_resources_remoting_xdocreport_opensagres_fr__binaryData_getMimeType() { return this._mimeType;}

services_resources_remoting_xdocreport_opensagres_fr__binaryData.prototype.getMimeType = services_resources_remoting_xdocreport_opensagres_fr__binaryData_getMimeType;

function services_resources_remoting_xdocreport_opensagres_fr__binaryData_setMimeType(value) { this._mimeType = value;}

services_resources_remoting_xdocreport_opensagres_fr__binaryData.prototype.setMimeType = services_resources_remoting_xdocreport_opensagres_fr__binaryData_setMimeType;
//
// accessor is services_resources_remoting_xdocreport_opensagres_fr__binaryData.prototype.getResourceId
// element get for resourceId
// - element type is {http://www.w3.org/2001/XMLSchema}string
// - optional element
//
// element set for resourceId
// setter function is is services_resources_remoting_xdocreport_opensagres_fr__binaryData.prototype.setResourceId
//
function services_resources_remoting_xdocreport_opensagres_fr__binaryData_getResourceId() { return this._resourceId;}

services_resources_remoting_xdocreport_opensagres_fr__binaryData.prototype.getResourceId = services_resources_remoting_xdocreport_opensagres_fr__binaryData_getResourceId;

function services_resources_remoting_xdocreport_opensagres_fr__binaryData_setResourceId(value) { this._resourceId = value;}

services_resources_remoting_xdocreport_opensagres_fr__binaryData.prototype.setResourceId = services_resources_remoting_xdocreport_opensagres_fr__binaryData_setResourceId;
//
// Serialize {http://services.resources.remoting.xdocreport.opensagres.fr/}binaryData
//
function services_resources_remoting_xdocreport_opensagres_fr__binaryData_serialize(cxfjsutils, elementName, extraNamespaces) {
    var xml = '';
    if (elementName != null) {
     xml = xml + '<';
     xml = xml + elementName;
     if (extraNamespaces) {
      xml = xml + ' ' + extraNamespaces;
     }
     xml = xml + '>';
    }
    // block for local variables
    {
     if (this._content != null) {
      xml = xml + '<content>';
      xml = xml + cxfjsutils.escapeXmlEntities(this._content);
      xml = xml + '</content>';
     }
    }
    // block for local variables
    {
     if (this._fileName != null) {
      xml = xml + '<fileName>';
      xml = xml + cxfjsutils.escapeXmlEntities(this._fileName);
      xml = xml + '</fileName>';
     }
    }
    // block for local variables
    {
     xml = xml + '<length>';
     xml = xml + cxfjsutils.escapeXmlEntities(this._length);
     xml = xml + '</length>';
    }
    // block for local variables
    {
     if (this._mimeType != null) {
      xml = xml + '<mimeType>';
      xml = xml + cxfjsutils.escapeXmlEntities(this._mimeType);
      xml = xml + '</mimeType>';
     }
    }
    // block for local variables
    {
     if (this._resourceId != null) {
      xml = xml + '<resourceId>';
      xml = xml + cxfjsutils.escapeXmlEntities(this._resourceId);
      xml = xml + '</resourceId>';
     }
    }
    if (elementName != null) {
     xml = xml + '</';
     xml = xml + elementName;
     xml = xml + '>';
    }
    return xml;
}

services_resources_remoting_xdocreport_opensagres_fr__binaryData.prototype.serialize = services_resources_remoting_xdocreport_opensagres_fr__binaryData_serialize;

function services_resources_remoting_xdocreport_opensagres_fr__binaryData_deserialize (cxfjsutils, element) {
    var newobject = new services_resources_remoting_xdocreport_opensagres_fr__binaryData();
    cxfjsutils.trace('element: ' + cxfjsutils.traceElementName(element));
    var curElement = cxfjsutils.getFirstElementChild(element);
    var item;
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing content');
    if (curElement != null && cxfjsutils.isNodeNamedNS(curElement, '', 'content')) {
     var value = null;
     if (!cxfjsutils.isElementNil(curElement)) {
      item = cxfjsutils.deserializeBase64orMom(curElement);
     }
     newobject.setContent(item);
     var item = null;
     if (curElement != null) {
      curElement = cxfjsutils.getNextElementSibling(curElement);
     }
    }
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing fileName');
    if (curElement != null && cxfjsutils.isNodeNamedNS(curElement, '', 'fileName')) {
     var value = null;
     if (!cxfjsutils.isElementNil(curElement)) {
      value = cxfjsutils.getNodeText(curElement);
      item = value;
     }
     newobject.setFileName(item);
     var item = null;
     if (curElement != null) {
      curElement = cxfjsutils.getNextElementSibling(curElement);
     }
    }
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing length');
    var value = null;
    if (!cxfjsutils.isElementNil(curElement)) {
     value = cxfjsutils.getNodeText(curElement);
     item = parseInt(value);
    }
    newobject.setLength(item);
    var item = null;
    if (curElement != null) {
     curElement = cxfjsutils.getNextElementSibling(curElement);
    }
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing mimeType');
    if (curElement != null && cxfjsutils.isNodeNamedNS(curElement, '', 'mimeType')) {
     var value = null;
     if (!cxfjsutils.isElementNil(curElement)) {
      value = cxfjsutils.getNodeText(curElement);
      item = value;
     }
     newobject.setMimeType(item);
     var item = null;
     if (curElement != null) {
      curElement = cxfjsutils.getNextElementSibling(curElement);
     }
    }
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing resourceId');
    if (curElement != null && cxfjsutils.isNodeNamedNS(curElement, '', 'resourceId')) {
     var value = null;
     if (!cxfjsutils.isElementNil(curElement)) {
      value = cxfjsutils.getNodeText(curElement);
      item = value;
     }
     newobject.setResourceId(item);
     var item = null;
     if (curElement != null) {
      curElement = cxfjsutils.getNextElementSibling(curElement);
     }
    }
    return newobject;
}

//
// Simple type (enumeration) {http://services.resources.remoting.xdocreport.opensagres.fr/}resourceType
//
// - FOLDER
// - FILE
//
// Constructor for XML Schema item {http://services.resources.remoting.xdocreport.opensagres.fr/}filter
//
function services_resources_remoting_xdocreport_opensagres_fr__filter () {
    this.typeMarker = 'services_resources_remoting_xdocreport_opensagres_fr__filter';
    this._properties = [];
}

//
// accessor is services_resources_remoting_xdocreport_opensagres_fr__filter.prototype.getProperties
// element get for properties
// - element type is {http://services.resources.remoting.xdocreport.opensagres.fr/}propertyRepresentation
// - required element
// - array
// - nillable
//
// element set for properties
// setter function is is services_resources_remoting_xdocreport_opensagres_fr__filter.prototype.setProperties
//
function services_resources_remoting_xdocreport_opensagres_fr__filter_getProperties() { return this._properties;}

services_resources_remoting_xdocreport_opensagres_fr__filter.prototype.getProperties = services_resources_remoting_xdocreport_opensagres_fr__filter_getProperties;

function services_resources_remoting_xdocreport_opensagres_fr__filter_setProperties(value) { this._properties = value;}

services_resources_remoting_xdocreport_opensagres_fr__filter.prototype.setProperties = services_resources_remoting_xdocreport_opensagres_fr__filter_setProperties;
//
// Serialize {http://services.resources.remoting.xdocreport.opensagres.fr/}filter
//
function services_resources_remoting_xdocreport_opensagres_fr__filter_serialize(cxfjsutils, elementName, extraNamespaces) {
    var xml = '';
    if (elementName != null) {
     xml = xml + '<';
     xml = xml + elementName;
     if (extraNamespaces) {
      xml = xml + ' ' + extraNamespaces;
     }
     xml = xml + '>';
    }
    // block for local variables
    {
     if (this._properties != null) {
      for (var ax = 0;ax < this._properties.length;ax ++) {
       if (this._properties[ax] == null) {
        xml = xml + '<properties xsi:nil=\'true\'/>';
       } else {
        xml = xml + this._properties[ax].serialize(cxfjsutils, 'properties', null);
       }
      }
     }
    }
    if (elementName != null) {
     xml = xml + '</';
     xml = xml + elementName;
     xml = xml + '>';
    }
    return xml;
}

services_resources_remoting_xdocreport_opensagres_fr__filter.prototype.serialize = services_resources_remoting_xdocreport_opensagres_fr__filter_serialize;

function services_resources_remoting_xdocreport_opensagres_fr__filter_deserialize (cxfjsutils, element) {
    var newobject = new services_resources_remoting_xdocreport_opensagres_fr__filter();
    cxfjsutils.trace('element: ' + cxfjsutils.traceElementName(element));
    var curElement = cxfjsutils.getFirstElementChild(element);
    var item;
    cxfjsutils.trace('curElement: ' + cxfjsutils.traceElementName(curElement));
    cxfjsutils.trace('processing properties');
    if (curElement != null && cxfjsutils.isNodeNamedNS(curElement, '', 'properties')) {
     item = [];
     do  {
      var arrayItem;
      var value = null;
      if (!cxfjsutils.isElementNil(curElement)) {
       arrayItem = services_resources_remoting_xdocreport_opensagres_fr__propertyRepresentation_deserialize(cxfjsutils, curElement);
      }
      item.push(arrayItem);
      curElement = cxfjsutils.getNextElementSibling(curElement);
     }
       while(curElement != null && cxfjsutils.isNodeNamedNS(curElement, '', 'properties'));
     newobject.setProperties(item);
     var item = null;
    }
    return newobject;
}

//
// Constructor for XML Schema item {http://services.resources.remoting.xdocreport.opensagres.fr/}ResourcesException
//
function services_resources_remoting_xdocreport_opensagres_fr__ResourcesException () {
    this.typeMarker = 'services_resources_remoting_xdocreport_opensagres_fr__ResourcesException';
}

//
// Serialize {http://services.resources.remoting.xdocreport.opensagres.fr/}ResourcesException
//
function services_resources_remoting_xdocreport_opensagres_fr__ResourcesException_serialize(cxfjsutils, elementName, extraNamespaces) {
    var xml = '';
    if (elementName != null) {
     xml = xml + '<';
     xml = xml + elementName;
     if (extraNamespaces) {
      xml = xml + ' ' + extraNamespaces;
     }
     xml = xml + '>';
    }
    if (elementName != null) {
     xml = xml + '</';
     xml = xml + elementName;
     xml = xml + '>';
    }
    return xml;
}

services_resources_remoting_xdocreport_opensagres_fr__ResourcesException.prototype.serialize = services_resources_remoting_xdocreport_opensagres_fr__ResourcesException_serialize;

function services_resources_remoting_xdocreport_opensagres_fr__ResourcesException_deserialize (cxfjsutils, element) {
    var newobject = new services_resources_remoting_xdocreport_opensagres_fr__ResourcesException();
    cxfjsutils.trace('element: ' + cxfjsutils.traceElementName(element));
    var curElement = cxfjsutils.getFirstElementChild(element);
    var item;
    return newobject;
}

//
// Definitions for service: {http://services.resources.remoting.xdocreport.opensagres.fr/}JAXWSResourcesServiceService
//

// Javascript for {http://services.resources.remoting.xdocreport.opensagres.fr/}ResourcesService

function services_resources_remoting_xdocreport_opensagres_fr__ResourcesService () {
    this.jsutils = new CxfApacheOrgUtil();
    this.jsutils.interfaceObject = this;
    this.synchronous = false;
    this.url = null;
    this.client = null;
    this.response = null;
    this.globalElementSerializers = [];
    this.globalElementDeserializers = [];
    this.globalElementSerializers['{http://services.resources.remoting.xdocreport.opensagres.fr/}resource'] = services_resources_remoting_xdocreport_opensagres_fr__resource_serialize;
    this.globalElementDeserializers['{http://services.resources.remoting.xdocreport.opensagres.fr/}resource'] = services_resources_remoting_xdocreport_opensagres_fr__resource_deserialize;
    this.globalElementSerializers['{http://services.resources.remoting.xdocreport.opensagres.fr/}binaryData'] = services_resources_remoting_xdocreport_opensagres_fr__binaryData_serialize;
    this.globalElementDeserializers['{http://services.resources.remoting.xdocreport.opensagres.fr/}binaryData'] = services_resources_remoting_xdocreport_opensagres_fr__binaryData_deserialize;
    this.globalElementSerializers['{http://services.resources.remoting.xdocreport.opensagres.fr/}ResourcesException'] = services_resources_remoting_xdocreport_opensagres_fr__ResourcesException_serialize;
    this.globalElementDeserializers['{http://services.resources.remoting.xdocreport.opensagres.fr/}ResourcesException'] = services_resources_remoting_xdocreport_opensagres_fr__ResourcesException_deserialize;
    this.globalElementSerializers['{http://services.resources.remoting.xdocreport.opensagres.fr/}resource'] = services_resources_remoting_xdocreport_opensagres_fr__resource_serialize;
    this.globalElementDeserializers['{http://services.resources.remoting.xdocreport.opensagres.fr/}resource'] = services_resources_remoting_xdocreport_opensagres_fr__resource_deserialize;
    this.globalElementSerializers['{http://services.resources.remoting.xdocreport.opensagres.fr/}propertyRepresentation'] = services_resources_remoting_xdocreport_opensagres_fr__propertyRepresentation_serialize;
    this.globalElementDeserializers['{http://services.resources.remoting.xdocreport.opensagres.fr/}propertyRepresentation'] = services_resources_remoting_xdocreport_opensagres_fr__propertyRepresentation_deserialize;
    this.globalElementSerializers['{http://services.resources.remoting.xdocreport.opensagres.fr/}binaryData'] = services_resources_remoting_xdocreport_opensagres_fr__binaryData_serialize;
    this.globalElementDeserializers['{http://services.resources.remoting.xdocreport.opensagres.fr/}binaryData'] = services_resources_remoting_xdocreport_opensagres_fr__binaryData_deserialize;
    this.globalElementSerializers['{http://services.resources.remoting.xdocreport.opensagres.fr/}filter'] = services_resources_remoting_xdocreport_opensagres_fr__filter_serialize;
    this.globalElementDeserializers['{http://services.resources.remoting.xdocreport.opensagres.fr/}filter'] = services_resources_remoting_xdocreport_opensagres_fr__filter_deserialize;
    this.globalElementSerializers['{http://services.resources.remoting.xdocreport.opensagres.fr/}ResourcesException'] = services_resources_remoting_xdocreport_opensagres_fr__ResourcesException_serialize;
    this.globalElementDeserializers['{http://services.resources.remoting.xdocreport.opensagres.fr/}ResourcesException'] = services_resources_remoting_xdocreport_opensagres_fr__ResourcesException_deserialize;
}

function services_resources_remoting_xdocreport_opensagres_fr__getRoot_op_onsuccess(client, responseXml) {
    if (client.user_onsuccess) {
     var responseObject = null;
     var element = responseXml.documentElement;
     this.jsutils.trace('responseXml: ' + this.jsutils.traceElementName(element));
     element = this.jsutils.getFirstElementChild(element);
     this.jsutils.trace('first element child: ' + this.jsutils.traceElementName(element));
     while (!this.jsutils.isNodeNamedNS(element, 'http://schemas.xmlsoap.org/soap/envelope/', 'Body')) {
      element = this.jsutils.getNextElementSibling(element);
      if (element == null) {
       throw 'No env:Body in message.'
      }
     }
     element = this.jsutils.getFirstElementChild(element);
     this.jsutils.trace('part element: ' + this.jsutils.traceElementName(element));
     this.jsutils.trace('calling services_resources_remoting_xdocreport_opensagres_fr__getRootResponse_deserializeResponse');
     responseObject = services_resources_remoting_xdocreport_opensagres_fr__getRootResponse_deserializeResponse(this.jsutils, element);
     client.user_onsuccess(responseObject);
    }
}

services_resources_remoting_xdocreport_opensagres_fr__ResourcesService.prototype.getRoot_onsuccess = services_resources_remoting_xdocreport_opensagres_fr__getRoot_op_onsuccess;

function services_resources_remoting_xdocreport_opensagres_fr__getRoot_op_onerror(client) {
    if (client.user_onerror) {
     var httpStatus;
     var httpStatusText;
     try {
      httpStatus = client.req.status;
      httpStatusText = client.req.statusText;
     } catch(e) {
      httpStatus = -1;
      httpStatusText = 'Error opening connection to server';
     }
     client.user_onerror(httpStatus, httpStatusText);
    }
}

services_resources_remoting_xdocreport_opensagres_fr__ResourcesService.prototype.getRoot_onerror = services_resources_remoting_xdocreport_opensagres_fr__getRoot_op_onerror;

//
// Operation {http://services.resources.remoting.xdocreport.opensagres.fr/}getRoot
// - bare operation. Parameters:
//
function services_resources_remoting_xdocreport_opensagres_fr__getRoot_op(successCallback, errorCallback) {
    this.client = new CxfApacheOrgClient(this.jsutils);
    var xml = null;
    var args = new Array(0);
    xml = this.getRoot_serializeInput(this.jsutils, args);
    this.client.user_onsuccess = successCallback;
    this.client.user_onerror = errorCallback;
    var closureThis = this;
    this.client.onsuccess = function(client, responseXml) { closureThis.getRoot_onsuccess(client, responseXml); };
    this.client.onerror = function(client) { closureThis.getRoot_onerror(client); };
    var requestHeaders = [];
    requestHeaders['SOAPAction'] = 'urn:GetRoot';
    this.jsutils.trace('synchronous = ' + this.synchronous);
    this.client.request(this.url, xml, null, this.synchronous, requestHeaders);
}

services_resources_remoting_xdocreport_opensagres_fr__ResourcesService.prototype.getRoot = services_resources_remoting_xdocreport_opensagres_fr__getRoot_op;

function services_resources_remoting_xdocreport_opensagres_fr__getRoot_serializeInput(cxfjsutils, args) {
    var xml;
    xml = cxfjsutils.beginSoap11Message("");
    xml = xml + cxfjsutils.endSoap11Message();
    return xml;
}

services_resources_remoting_xdocreport_opensagres_fr__ResourcesService.prototype.getRoot_serializeInput = services_resources_remoting_xdocreport_opensagres_fr__getRoot_serializeInput;

function services_resources_remoting_xdocreport_opensagres_fr__getRootResponse_deserializeResponse(cxfjsutils, partElement) {
    var returnObject = services_resources_remoting_xdocreport_opensagres_fr__resource_deserialize (cxfjsutils, partElement);

    return returnObject;
}
function services_resources_remoting_xdocreport_opensagres_fr__download1_op_onsuccess(client, responseXml) {
    if (client.user_onsuccess) {
     var responseObject = null;
     var element = responseXml.documentElement;
     this.jsutils.trace('responseXml: ' + this.jsutils.traceElementName(element));
     element = this.jsutils.getFirstElementChild(element);
     this.jsutils.trace('first element child: ' + this.jsutils.traceElementName(element));
     while (!this.jsutils.isNodeNamedNS(element, 'http://schemas.xmlsoap.org/soap/envelope/', 'Body')) {
      element = this.jsutils.getNextElementSibling(element);
      if (element == null) {
       throw 'No env:Body in message.'
      }
     }
     element = this.jsutils.getFirstElementChild(element);
     this.jsutils.trace('part element: ' + this.jsutils.traceElementName(element));
     this.jsutils.trace('calling services_resources_remoting_xdocreport_opensagres_fr__download1Response_deserializeResponse');
     responseObject = services_resources_remoting_xdocreport_opensagres_fr__download1Response_deserializeResponse(this.jsutils, element);
     client.user_onsuccess(responseObject);
    }
}

services_resources_remoting_xdocreport_opensagres_fr__ResourcesService.prototype.download1_onsuccess = services_resources_remoting_xdocreport_opensagres_fr__download1_op_onsuccess;

function services_resources_remoting_xdocreport_opensagres_fr__download1_op_onerror(client) {
    if (client.user_onerror) {
     var httpStatus;
     var httpStatusText;
     try {
      httpStatus = client.req.status;
      httpStatusText = client.req.statusText;
     } catch(e) {
      httpStatus = -1;
      httpStatusText = 'Error opening connection to server';
     }
     client.user_onerror(httpStatus, httpStatusText);
    }
}

services_resources_remoting_xdocreport_opensagres_fr__ResourcesService.prototype.download1_onerror = services_resources_remoting_xdocreport_opensagres_fr__download1_op_onerror;

//
// Operation {http://services.resources.remoting.xdocreport.opensagres.fr/}download1
// - bare operation. Parameters:
// - type {http://www.w3.org/2001/XMLSchema}string
//
function services_resources_remoting_xdocreport_opensagres_fr__download1_op(successCallback, errorCallback, resourceId) {
    this.client = new CxfApacheOrgClient(this.jsutils);
    var xml = null;
    var args = new Array(1);
    args[0] = resourceId;
    xml = this.download1_serializeInput(this.jsutils, args);
    this.client.user_onsuccess = successCallback;
    this.client.user_onerror = errorCallback;
    var closureThis = this;
    this.client.onsuccess = function(client, responseXml) { closureThis.download1_onsuccess(client, responseXml); };
    this.client.onerror = function(client) { closureThis.download1_onerror(client); };
    var requestHeaders = [];
    requestHeaders['SOAPAction'] = 'urn:Download1';
    this.jsutils.trace('synchronous = ' + this.synchronous);
    this.client.request(this.url, xml, null, this.synchronous, requestHeaders);
}

services_resources_remoting_xdocreport_opensagres_fr__ResourcesService.prototype.download1 = services_resources_remoting_xdocreport_opensagres_fr__download1_op;

function services_resources_remoting_xdocreport_opensagres_fr__download1_serializeInput(cxfjsutils, args) {
    var xml;
    xml = cxfjsutils.beginSoap11Message("");
    // block for local variables
    {
     xml = xml + '<resourceId>';
     xml = xml + cxfjsutils.escapeXmlEntities(args[0]);
     xml = xml + '</resourceId>';
    }
    xml = xml + cxfjsutils.endSoap11Message();
    return xml;
}

services_resources_remoting_xdocreport_opensagres_fr__ResourcesService.prototype.download1_serializeInput = services_resources_remoting_xdocreport_opensagres_fr__download1_serializeInput;

function services_resources_remoting_xdocreport_opensagres_fr__download1Response_deserializeResponse(cxfjsutils, partElement) {
    var returnObject = services_resources_remoting_xdocreport_opensagres_fr__binaryData_deserialize (cxfjsutils, partElement);

    return returnObject;
}
function services_resources_remoting_xdocreport_opensagres_fr__getName_op_onsuccess(client, responseXml) {
    if (client.user_onsuccess) {
     var responseObject = null;
     var element = responseXml.documentElement;
     this.jsutils.trace('responseXml: ' + this.jsutils.traceElementName(element));
     element = this.jsutils.getFirstElementChild(element);
     this.jsutils.trace('first element child: ' + this.jsutils.traceElementName(element));
     while (!this.jsutils.isNodeNamedNS(element, 'http://schemas.xmlsoap.org/soap/envelope/', 'Body')) {
      element = this.jsutils.getNextElementSibling(element);
      if (element == null) {
       throw 'No env:Body in message.'
      }
     }
     element = this.jsutils.getFirstElementChild(element);
     this.jsutils.trace('part element: ' + this.jsutils.traceElementName(element));
     this.jsutils.trace('calling services_resources_remoting_xdocreport_opensagres_fr__getNameResponse_deserializeResponse');
     responseObject = services_resources_remoting_xdocreport_opensagres_fr__getNameResponse_deserializeResponse(this.jsutils, element);
     client.user_onsuccess(responseObject);
    }
}

services_resources_remoting_xdocreport_opensagres_fr__ResourcesService.prototype.getName_onsuccess = services_resources_remoting_xdocreport_opensagres_fr__getName_op_onsuccess;

function services_resources_remoting_xdocreport_opensagres_fr__getName_op_onerror(client) {
    if (client.user_onerror) {
     var httpStatus;
     var httpStatusText;
     try {
      httpStatus = client.req.status;
      httpStatusText = client.req.statusText;
     } catch(e) {
      httpStatus = -1;
      httpStatusText = 'Error opening connection to server';
     }
     client.user_onerror(httpStatus, httpStatusText);
    }
}

services_resources_remoting_xdocreport_opensagres_fr__ResourcesService.prototype.getName_onerror = services_resources_remoting_xdocreport_opensagres_fr__getName_op_onerror;

//
// Operation {http://services.resources.remoting.xdocreport.opensagres.fr/}getName
// - bare operation. Parameters:
//
function services_resources_remoting_xdocreport_opensagres_fr__getName_op(successCallback, errorCallback) {
    this.client = new CxfApacheOrgClient(this.jsutils);
    var xml = null;
    var args = new Array(0);
    xml = this.getName_serializeInput(this.jsutils, args);
    this.client.user_onsuccess = successCallback;
    this.client.user_onerror = errorCallback;
    var closureThis = this;
    this.client.onsuccess = function(client, responseXml) { closureThis.getName_onsuccess(client, responseXml); };
    this.client.onerror = function(client) { closureThis.getName_onerror(client); };
    var requestHeaders = [];
    requestHeaders['SOAPAction'] = 'urn:GetName';
    this.jsutils.trace('synchronous = ' + this.synchronous);
    this.client.request(this.url, xml, null, this.synchronous, requestHeaders);
}

services_resources_remoting_xdocreport_opensagres_fr__ResourcesService.prototype.getName = services_resources_remoting_xdocreport_opensagres_fr__getName_op;

function services_resources_remoting_xdocreport_opensagres_fr__getName_serializeInput(cxfjsutils, args) {
    var xml;
    xml = cxfjsutils.beginSoap11Message("");
    xml = xml + cxfjsutils.endSoap11Message();
    return xml;
}

services_resources_remoting_xdocreport_opensagres_fr__ResourcesService.prototype.getName_serializeInput = services_resources_remoting_xdocreport_opensagres_fr__getName_serializeInput;

function services_resources_remoting_xdocreport_opensagres_fr__getNameResponse_deserializeResponse(cxfjsutils, partElement) {
    var returnText = cxfjsutils.getNodeText(partElement);
    var returnObject = returnText;
    return returnObject;
}
function services_resources_remoting_xdocreport_opensagres_fr__upload_op_onsuccess(client) {
    if (client.user_onsuccess) {
     var responseObject = null;
     client.user_onsuccess(responseObject);
    }
}

services_resources_remoting_xdocreport_opensagres_fr__ResourcesService.prototype.upload_onsuccess = services_resources_remoting_xdocreport_opensagres_fr__upload_op_onsuccess;

function services_resources_remoting_xdocreport_opensagres_fr__upload_op_onerror(client) {
    if (client.user_onerror) {
     var httpStatus;
     var httpStatusText;
     try {
      httpStatus = client.req.status;
      httpStatusText = client.req.statusText;
     } catch(e) {
      httpStatus = -1;
      httpStatusText = 'Error opening connection to server';
     }
     client.user_onerror(httpStatus, httpStatusText);
    }
}

services_resources_remoting_xdocreport_opensagres_fr__ResourcesService.prototype.upload_onerror = services_resources_remoting_xdocreport_opensagres_fr__upload_op_onerror;

//
// Operation {http://services.resources.remoting.xdocreport.opensagres.fr/}upload
// - bare operation. Parameters:
// - services_resources_remoting_xdocreport_opensagres_fr__binaryData
//
function services_resources_remoting_xdocreport_opensagres_fr__upload_op(successCallback, errorCallback, content) {
    this.client = new CxfApacheOrgClient(this.jsutils);
    var xml = null;
    var args = new Array(1);
    args[0] = content;
    xml = this.upload_serializeInput(this.jsutils, args);
    this.client.user_onsuccess = successCallback;
    this.client.user_onerror = errorCallback;
    var closureThis = this;
    this.client.onsuccess = function(client, responseXml) { closureThis.upload_onsuccess(client, responseXml); };
    this.client.onerror = function(client) { closureThis.upload_onerror(client); };
    var requestHeaders = [];
    requestHeaders['SOAPAction'] = 'urn:Upload';
    this.jsutils.trace('synchronous = ' + this.synchronous);
    this.client.request(this.url, xml, null, this.synchronous, requestHeaders);
}

services_resources_remoting_xdocreport_opensagres_fr__ResourcesService.prototype.upload = services_resources_remoting_xdocreport_opensagres_fr__upload_op;

function services_resources_remoting_xdocreport_opensagres_fr__upload_serializeInput(cxfjsutils, args) {
    var xml;
    xml = cxfjsutils.beginSoap11Message("");
    // block for local variables
    {
     xml = xml + args[0].serialize(cxfjsutils, 'content', null);
    }
    xml = xml + cxfjsutils.endSoap11Message();
    return xml;
}

services_resources_remoting_xdocreport_opensagres_fr__ResourcesService.prototype.upload_serializeInput = services_resources_remoting_xdocreport_opensagres_fr__upload_serializeInput;

function services_resources_remoting_xdocreport_opensagres_fr__getRoot1_op_onsuccess(client, responseXml) {
    if (client.user_onsuccess) {
     var responseObject = null;
     var element = responseXml.documentElement;
     this.jsutils.trace('responseXml: ' + this.jsutils.traceElementName(element));
     element = this.jsutils.getFirstElementChild(element);
     this.jsutils.trace('first element child: ' + this.jsutils.traceElementName(element));
     while (!this.jsutils.isNodeNamedNS(element, 'http://schemas.xmlsoap.org/soap/envelope/', 'Body')) {
      element = this.jsutils.getNextElementSibling(element);
      if (element == null) {
       throw 'No env:Body in message.'
      }
     }
     element = this.jsutils.getFirstElementChild(element);
     this.jsutils.trace('part element: ' + this.jsutils.traceElementName(element));
     this.jsutils.trace('calling services_resources_remoting_xdocreport_opensagres_fr__getRoot1Response_deserializeResponse');
     responseObject = services_resources_remoting_xdocreport_opensagres_fr__getRoot1Response_deserializeResponse(this.jsutils, element);
     client.user_onsuccess(responseObject);
    }
}

services_resources_remoting_xdocreport_opensagres_fr__ResourcesService.prototype.getRoot1_onsuccess = services_resources_remoting_xdocreport_opensagres_fr__getRoot1_op_onsuccess;

function services_resources_remoting_xdocreport_opensagres_fr__getRoot1_op_onerror(client) {
    if (client.user_onerror) {
     var httpStatus;
     var httpStatusText;
     try {
      httpStatus = client.req.status;
      httpStatusText = client.req.statusText;
     } catch(e) {
      httpStatus = -1;
      httpStatusText = 'Error opening connection to server';
     }
     client.user_onerror(httpStatus, httpStatusText);
    }
}

services_resources_remoting_xdocreport_opensagres_fr__ResourcesService.prototype.getRoot1_onerror = services_resources_remoting_xdocreport_opensagres_fr__getRoot1_op_onerror;

//
// Operation {http://services.resources.remoting.xdocreport.opensagres.fr/}getRoot1
// - bare operation. Parameters:
// - services_resources_remoting_xdocreport_opensagres_fr__filter
//
function services_resources_remoting_xdocreport_opensagres_fr__getRoot1_op(successCallback, errorCallback, filter) {
    this.client = new CxfApacheOrgClient(this.jsutils);
    var xml = null;
    var args = new Array(1);
    args[0] = filter;
    xml = this.getRoot1_serializeInput(this.jsutils, args);
    this.client.user_onsuccess = successCallback;
    this.client.user_onerror = errorCallback;
    var closureThis = this;
    this.client.onsuccess = function(client, responseXml) { closureThis.getRoot1_onsuccess(client, responseXml); };
    this.client.onerror = function(client) { closureThis.getRoot1_onerror(client); };
    var requestHeaders = [];
    requestHeaders['SOAPAction'] = 'urn:GetRoot1';
    this.jsutils.trace('synchronous = ' + this.synchronous);
    this.client.request(this.url, xml, null, this.synchronous, requestHeaders);
}

services_resources_remoting_xdocreport_opensagres_fr__ResourcesService.prototype.getRoot1 = services_resources_remoting_xdocreport_opensagres_fr__getRoot1_op;

function services_resources_remoting_xdocreport_opensagres_fr__getRoot1_serializeInput(cxfjsutils, args) {
    var xml;
    xml = cxfjsutils.beginSoap11Message("");
    // block for local variables
    {
     xml = xml + args[0].serialize(cxfjsutils, 'filter', null);
    }
    xml = xml + cxfjsutils.endSoap11Message();
    return xml;
}

services_resources_remoting_xdocreport_opensagres_fr__ResourcesService.prototype.getRoot1_serializeInput = services_resources_remoting_xdocreport_opensagres_fr__getRoot1_serializeInput;

function services_resources_remoting_xdocreport_opensagres_fr__getRoot1Response_deserializeResponse(cxfjsutils, partElement) {
    var returnObject = services_resources_remoting_xdocreport_opensagres_fr__resource_deserialize (cxfjsutils, partElement);

    return returnObject;
}
function services_resources_remoting_xdocreport_opensagres_fr__download_op_onsuccess(client, responseXml) {
    if (client.user_onsuccess) {
     var responseObject = null;
     var element = responseXml.documentElement;
     this.jsutils.trace('responseXml: ' + this.jsutils.traceElementName(element));
     element = this.jsutils.getFirstElementChild(element);
     this.jsutils.trace('first element child: ' + this.jsutils.traceElementName(element));
     while (!this.jsutils.isNodeNamedNS(element, 'http://schemas.xmlsoap.org/soap/envelope/', 'Body')) {
      element = this.jsutils.getNextElementSibling(element);
      if (element == null) {
       throw 'No env:Body in message.'
      }
     }
     element = this.jsutils.getFirstElementChild(element);
     this.jsutils.trace('part element: ' + this.jsutils.traceElementName(element));
     this.jsutils.trace('calling services_resources_remoting_xdocreport_opensagres_fr__downloadResponse_deserializeResponse');
     responseObject = services_resources_remoting_xdocreport_opensagres_fr__downloadResponse_deserializeResponse(this.jsutils, element);
     client.user_onsuccess(responseObject);
    }
}

services_resources_remoting_xdocreport_opensagres_fr__ResourcesService.prototype.download_onsuccess = services_resources_remoting_xdocreport_opensagres_fr__download_op_onsuccess;

function services_resources_remoting_xdocreport_opensagres_fr__download_op_onerror(client) {
    if (client.user_onerror) {
     var httpStatus;
     var httpStatusText;
     try {
      httpStatus = client.req.status;
      httpStatusText = client.req.statusText;
     } catch(e) {
      httpStatus = -1;
      httpStatusText = 'Error opening connection to server';
     }
     client.user_onerror(httpStatus, httpStatusText);
    }
}

services_resources_remoting_xdocreport_opensagres_fr__ResourcesService.prototype.download_onerror = services_resources_remoting_xdocreport_opensagres_fr__download_op_onerror;

//
// Operation {http://services.resources.remoting.xdocreport.opensagres.fr/}download
// - bare operation. Parameters:
// - type {http://www.w3.org/2001/XMLSchema}string
//
function services_resources_remoting_xdocreport_opensagres_fr__download_op(successCallback, errorCallback, resourceIds) {
    this.client = new CxfApacheOrgClient(this.jsutils);
    var xml = null;
    var args = new Array(1);
    args[0] = resourceIds;
    xml = this.download_serializeInput(this.jsutils, args);
    this.client.user_onsuccess = successCallback;
    this.client.user_onerror = errorCallback;
    var closureThis = this;
    this.client.onsuccess = function(client, responseXml) { closureThis.download_onsuccess(client, responseXml); };
    this.client.onerror = function(client) { closureThis.download_onerror(client); };
    var requestHeaders = [];
    requestHeaders['SOAPAction'] = 'urn:Download';
    this.jsutils.trace('synchronous = ' + this.synchronous);
    this.client.request(this.url, xml, null, this.synchronous, requestHeaders);
}

services_resources_remoting_xdocreport_opensagres_fr__ResourcesService.prototype.download = services_resources_remoting_xdocreport_opensagres_fr__download_op;

function services_resources_remoting_xdocreport_opensagres_fr__download_serializeInput(cxfjsutils, args) {
    var xml;
    xml = cxfjsutils.beginSoap11Message("");
    // block for local variables
    {
     xml = xml + '<resourceIds>';
     xml = xml + cxfjsutils.escapeXmlEntities(args[0]);
     xml = xml + '</resourceIds>';
    }
    xml = xml + cxfjsutils.endSoap11Message();
    return xml;
}

services_resources_remoting_xdocreport_opensagres_fr__ResourcesService.prototype.download_serializeInput = services_resources_remoting_xdocreport_opensagres_fr__download_serializeInput;

function services_resources_remoting_xdocreport_opensagres_fr__downloadResponse_deserializeResponse(cxfjsutils, partElement) {
    var returnObject = services_resources_remoting_xdocreport_opensagres_fr__binaryData_deserialize (cxfjsutils, partElement);

    return returnObject;
}
function services_resources_remoting_xdocreport_opensagres_fr__ResourcesService_services_resources_remoting_xdocreport_opensagres_fr__ResourcesServicePort () {
  this.url = 'http://localhost:9090/ResourcesServicePort';
}
services_resources_remoting_xdocreport_opensagres_fr__ResourcesService_services_resources_remoting_xdocreport_opensagres_fr__ResourcesServicePort.prototype = new services_resources_remoting_xdocreport_opensagres_fr__ResourcesService;
