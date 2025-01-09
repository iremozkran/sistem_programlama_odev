require 'socket'
require 'google/protobuf'
require_relative 'message_pb'

class Admin
  def initialize
    @servers = [
      TCPSocket.new('localhost', 5001),
      TCPSocket.new('localhost', 5002),
      TCPSocket.new('localhost', 5003)
    ]
  end

  def send_start_command
    message = Message.new(demand: :STRT)
    @servers.each do |server|
      server.write(message.to_proto)
      response = Message.decode(server.read(1024))
      puts "Response from server: #{response.response}"
    end
  end

  def send_capacity_query
    loop do
      sleep(5)
      message = Message.new(demand: :CPCTY)
      @servers.each { |server| server.write(message.to_proto) }
    end
  end
end

admin = Admin.new
admin.send_start_command
admin.send_capacity_query
