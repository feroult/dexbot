from services.Base import BaseService
import webapp2

class MainHandler(webapp2.RequestHandler):
    services = {'/services/base': BaseService()}

    def process(self, method):
        print 'Hello'
        if self.request.path in self.services:
            service = self.services[self.request.path]
            if method in service.methods:
                method = service.methods[method]
                method(self.request, self.response)
            else:
                self.response.status = 404
        else:
            self.response.status = 404
    
    def get(self):
        self.process('get')
    def post(self):
        self.process('post')
    def put(self):
        self.process('put')
    def delete(self):
        self.process('delete')

app = webapp2.WSGIApplication([
    ('/services/.*', MainHandler)
], debug=True)
