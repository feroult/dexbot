class Template:
    def get(request, response):
        response.write('Hello service')
    
    methods = {'get': get}