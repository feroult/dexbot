function template(args) {  
  return doT.template.apply(this, arguments);
}

function testDoT() {
  var tempFn = template("<h1>Here is a sample template {{=it.foo}}</h1>");  
  var resultText = tempFn({foo: 'with doT'});
  
  GSUnit.assertEquals("<h1>Here is a sample template with doT</h1>", resultText);
}
