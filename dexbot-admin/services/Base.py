from google.appengine.ext import db
import json

class template_base(db.Model):
    base = db.StringProperty(required=False, multiline=True)
    desc = db.StringProperty(required=True)

class BaseCrudService:
    def put(request, response):
        baseStr = request.get("base")
        base = json.loads(baseStr);

        id_key = key_name=base['id_key'];
        template_base = db.get(id_key);
        template_base.base = base['base'];
        template_base.desc = base['desc'];
        template_base.put();

    def get(request, response):
        id_key = request.get('id');
        template_base = db.get(id_key);
        resp = {'base': template_base.base, 'desc': template_base.desc, 'id_key': str(template_base.key())};
        response.out.write(json.dumps(resp));
        
    methods = {'get': get, 'put': put}
    
class BaseListService:
    def post(request, response):
        desc = request.get("desc")
        baseEntity = template_base(desc=desc)
        baseEntity.put();
        response.headers['Content-Type'] = 'application/json';
        response.out.write(str(baseEntity.key()));

    def get(request, response):
        q = template_base.all();
        result = [];
        
        for item in q.run(limit=5):
            result.append({'base': item.base, 'desc': item.desc, 'id_key': str(item.key())})

        response.headers['Content-Type'] = 'application/json';
        response.out.write(json.dumps(result));
        
    methods = {'post': post, 'get': get}