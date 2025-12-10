import jenkins.model.*
import hudson.security.*

def instance = Jenkins.getInstance()
def realm = new HudsonPrivateSecurityRealm(false)
realm.createAccount("admin", "admin123")
instance.setSecurityRealm(realm)
instance.save()
println("Admin password reset to: admin / admin123")
