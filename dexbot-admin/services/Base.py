from google.appengine.ext import db

class template_base(db.Model):
  base = db.StringProperty(required=True, multiline=True)

class BaseService:
    def post(request, response):
        base = request.get("base")
        baseEntity = template_base(key_name='BASE', base=base)
        baseEntity.put();

    def get(request, response):
        key = db.Key.from_path('template_base', 'BASE');
        template_base = db.get(key);
        resp = ''
        if template_base is not None:
            resp = template_base.base
        response.out.write(resp)
        
    methods = {'post': post, 'get': get}