import BaseHTTPServer
import socket
import cgi
import locomatix
import threading
import httplib
import time
import sys
import traceback
import random

try: import simplejson as json
except ImportError:
  try: import json
  except ImportError:
    raise ImportError("simplejson is not installed. Please download it from http://code.google.com/p/simplejson/")

class PostReaderHTTPServer(BaseHTTPServer.HTTPServer):
  def __init__(self, *args, **kwargs):
    BaseHTTPServer.HTTPServer.__init__(self, *args, **kwargs)
    self._stop = False
    self.lock = threading.Lock()
    self.data = []

  def add_data(self, data):
    self.lock.acquire()
    self.data.append(data)
    self.lock.release()

  def has_data(self):
    self.lock.acquire()
    hd = len(self.data) > 0
    self.lock.release()
    return hd

  def pop_data(self):
    self.lock.acquire()
    if len(self.data) == 0:
      data = None
    else:
      data = self.data.pop()
    self.lock.release()

    return data

  def serve_forever(self):
    while not self._stop:
      self.handle_request()

  def stop(self):
    self._stop = True

    # Could not find a way to shut down the HTTPServer in Python 2.5 without a final request
    # conn = httplib.HTTPConnection(self.server_name, self.server_port)
    # conn.request("NULL", "/")
    # conn.getresponse()
    # conn.close()

class PostReaderRequestHandler(BaseHTTPServer.BaseHTTPRequestHandler):

  # Set HTTP protocol version 1.1
  protocol_version = 'HTTP/1.1'

  def log_message(self, *args, **kwargs):
    pass

  def do_NULL(self):
    self.send_response(httplib.OK, "")
    self.end_headers()

  def do_POST(self):

    resp_str = "<HTML>POST OK.<BR><BR></HTML>"
    self.send_response(200)
    # self.send_header('Content-type', 'text/plain')
    self.send_header('Content-type', 'text/html')
    self.send_header("Content-Length", len(resp_str))
    self.end_headers()

    # self.wfile.write('Thanks!\n')
    self.wfile.write(resp_str)

    # Extract the input data
    length = int(self.headers.getheader('Content-Length'))
    data = self.rfile.read(length)
    self.server.add_data(data)

class PostReader(threading.Thread):
  def __init__(self, *args, **kwargs):
    hostname = socket.gethostname()
    self.server = PostReaderHTTPServer((hostname, 0), PostReaderRequestHandler)
    self.url = 'http://%s:%s' % (hostname, self.server.server_port)
    super(PostReader, self).__init__(*args, **kwargs)

  def run(self):
    self.server.serve_forever()

  def get_data(self, timeout=0):
    start_time = time.time()

    while True:
      data = self.server.pop_data()
      if data != None:
        return data

      if timeout != 0 and time.time() >= start_time + timeout:
        return None
      time.sleep(1)

    return self.server.data

  def stop(self):
    self.server.stop()

def parse_json_alert(data):
  result = cgi.parse_qs(data)
  try:
    alert_data = result['Alert'][0]
  except KeyError:
    return None
  except IndexError:
    return None

  alertjson = json.loads(alert_data)
  return alertjson

class AlertListener(threading.Thread):
  ""

  ""
  def __init__(self):
    threading.Thread.__init__(self)
    self.reader = PostReader()
    self.reader.start()
    self.stop_flag = False

  def run(self):

    # Wait for POST data
    while not self.stop_flag:
      data = self.reader.get_data(timeout=1)

      # Nothing to read, let us go and wait
      if data == None: continue

      json_data = parse_json_alert(data)
      try:
        feedname = json_data['Ingress'][0]['Feed']
        oid = json_data['Ingress'][0]['ObjectID']
      except (KeyError, IndexError):
        assert False, 'Incorrect callback from server: %s' % json

      print "Alert triggered by object %s in feed %s" % (oid, feedname) 

  def stop(self):
    self.stop_flag = True
    self.reader.stop()

if __name__ == '__main__':
  try:
 
    # Create the alert listener
    listener = AlertListener()
    listener.start()

    print "Alerts are being received at %s" % (listener.reader.url)

    # Wait for alerts to show up
    time.sleep(10000)

  except KeyboardInterrupt:
    listener.stop()
    sys.exit(0)

  except Exception, e:
    print 'Uncaught error:'
    listener.stop()
    traceback.print_exc()
    sys.exit(1)
