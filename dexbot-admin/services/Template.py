from google.appengine.ext import db
import json

class template(db.Model):
    service_url = db.StringProperty(required=False)
    template = db.StringProperty(required=True, multiline=True)
    base_key = db.IntegerProperty(required=True)


def templateFromJson(template, json):
    print json['baseKey'] 
    template.service_url = json['serviceUrl'];
    template.template = json['template'];
    template.base_key = long(json['baseKey']);
    return template;

class TemplateService:

    def delete(request, response):
        id_key = long(request.get('id_key'));
        k = db.Key.from_path('template', long(id_key));
        template = db.get(k);
        template.delete();
    
    def put(request, response):
        templateStr = request.get("template")
        templateJson = json.loads(templateStr);
        id_key = templateJson['id_key'];
    
        k = db.Key.from_path('template', long(id_key));
        template = db.get(k);
            
        templateFromJson(template, templateJson);
        template.put();

        response.headers['Content-Type'] = 'application/json';
        response.out.write(json.dumps(templateJson));

    def post(request, response):
        templateStr = request.get("template")

        templateJson = json.loads(templateStr);
        
        template_entity = template(service_url=templateJson['serviceUrl'],
                                   template=templateJson['template'],
                                   base_key=long(templateJson['baseKey']));
        template_entity.put();

        templateJson['id_key'] = template_entity.key().id();
        
        response.headers['Content-Type'] = 'application/json';
        response.out.write(json.dumps(templateJson));

    def get(request, response):
        base_key = long(request.get('base_key'));
        
        q = template.all();
        q.filter('base_key = ', base_key);
        
        result= [];
        for item in q.run():
            result.append({
                           'serviceUrl': item.service_url,
                           'template': item.template,
                           'baseKey': item.base_key,
                           'id_key': long(item.key().id())
                        });
        response.headers['Content-Type'] = 'application/json';
        response.out.write(json.dumps(result));

    methods = {'get': get, 'put': put, 'post': post, 'delete': delete}