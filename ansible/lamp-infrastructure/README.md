# Web Architecture Example - Ansible

This is a simple example of a complete web architecture configuration using Ansible to configure a set of VMs either on local infrastructure using VirtualBox and Vagrant (using the included Vagrantfile), or on a cloud hosting provider (in this case, DigitalOcean).

The architecture for the example web application will be:

                       --------------------------
                      |  varnish.test (Varnish)  |
                      |  192.168.2.2             |
                       --------------------------
                          /                   \
         ----------------------          ----------------------
        |  www1.test (Apache)  |        |  www2.test (Apache)  |
        |  192.168.2.3         |        |  192.168.2.4         |
         ----------------------          ----------------------
                          \                   /
                      ------------------------------
                     |  memcached.test (Memcached)  |
                     |  192.168.2.7                 |
                      ------------------------------
                          /                   \
     -----------------------------       ----------------------------
    |  db1.test (MySQL - Master)  |     |  db2.test (MySQL - Slave)  |
    |  192.168.2.5                |     |  192.168.2.6               |
     -----------------------------       ----------------------------

*IP addresses and hostnames in this diagram are modeled after local VirtualBox/Vagrant-based VMs.*

This architecture offers multiple levels of caching and high availability/redundancy on almost all levels, though to keep it simple, there are single points of failure. All persistent data stored in the database is stored in a slave server, and one of the slowest and most constrained parts of the stack (the web servers, in this case running a PHP application through Apache) is easy to scale horizontally, behind Varnish, which is acting as a caching (reverse proxy) layer and load balancer.

For the purpose of demonstration, Varnish's caching is completely disabled, so you can refresh and see both Apache servers (with caching enabled, Varnish would cache the first response then keep serving it without hitting the rest of the stack). You can see the caching and load balancing configuration in `playbooks/varnish/templates/default.vcl`).

## Prerequisites

Before you can run any of these playbooks, you will need to [install Ansible](https://docs.ansible.com/ansible/latest/installation_guide/intro_installation.html), and run the following command to download dependencies (from within the same directory as this README file):

    $ ansible-galaxy install -r requirements.yml

If you would like to build the infrastructure locally, you will also need to install the latest versions of [VirtualBox](https://www.virtualbox.org/wiki/Downloads) and [Vagrant](https://www.vagrantup.com/downloads.html).

## Build and configure the servers (Local)

To build the VMs and configure them using Ansible, follow these steps (both from within this directory):

  1. Run `vagrant up`.
  2. Run `ansible-playbook configure.yml -i inventories/vagrant/`.

This guide assumes you already have Vagrant, VirtualBox, and Ansible installed locally.

After everything is booted and configured, visit http://varnish.test/ (if you configured the domain in your hosts file with the line `192.168.2.2  varnish.test`) in a browser, and refresh a few times to see that Varnish, Apache, PHP, Memcached, and MySQL are all working properly!

## Build and configure the servers (DigitalOcean)

Pre-suppositions: You have a DigitalOcean account, and you have a Personal Access Token for your account. Additionally, you have `dopy` and Ansible installed on your workstation (install `dopy` with `sudo pip install dopy`).

To build the droplets and configure them using Ansible, follow these steps (both from within this directory):

  1. Set your DigitalOcean Personal Access Token: `export DO_API_TOKEN=[token here]`
  2. Run `ansible-playbook provision.yml`.

After everything is booted and configured, visit the IP address of the Varnish server that was created in your DigitalOcean account in a browser, and refresh a few times to see that Varnish, Apache, PHP, Memcached, and MySQL are all working properly!

### Notes

  - Public IP addresses are used for all cross-droplet communication (e.g. PHP to MySQL/Memcached communication, MySQL master/slave replication). For better security and potentially a tiny performance improvement, you can use droplets' `private_ip_address` for cross-droplet communication.
  - Hosting active or inactive droplets on DigitalOcean will incur hosting fees (normally $0.01 USD/hour for the default 512mb droplets used in this example). While the charges will be nominal (likely less than $1 USD for many hours of testing), it's important to destroy droplets you aren't actively using!
  - You can use the included `digital_ocean.py` inventory script for dynamic inventory (`python digital_ocean.py --pretty` to test).
