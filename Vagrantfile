# -*- mode: ruby -*-
# vi: set ft=ruby :

NODES = (6379..6381)

SLOTS = {"6379" => "0..5500", "6380" => "5501..11000", "6381" => "11001..16383"}

IP = "192.168.33.10"

Vagrant.configure("2") do |config|
  config.vm.box = "centos/7"
  config.vm.network "private_network", ip: IP

  config.vm.provider "virtualbox" do |vb|
    vb.memory = "1024"
  end

  config.vm.provision "docker" do |d|
    d.pull_images "redis:3.2.4"
  end

  config.vm.provision "shell", inline: "docker ps -aq | xargs -r docker stop | xargs -r docker rm"

  config.vm.provision "docker" do |d|
    NODES.each do |node|
      d.run "redis#{node}",
        image: "redis:3.2.4",
        args: "--net host -v /vagrant/conf/redis/#{node}:/data/conf",
        cmd: "redis-server /data/conf/redis.conf"
    end
  end

  SLOTS.each do |node, slot|
    config.vm.provision "shell", inline: "docker exec redis#{node} redis-cli -p #{node} cluster addslots {#{slot}}"
  end

  NODES.each do |node|
    config.vm.provision "shell", inline: "docker exec redis#{node} redis-cli -p #{node} cluster meet #{IP} 6379"
  end
end
