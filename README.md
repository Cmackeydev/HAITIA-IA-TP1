# TP1 - Charles (Jakarta EE / JSF / Gemini API)

## Commandes Payara

### Démarrer Payara
```bash
/home/mc/Documents/payara/payara6/bin/asadmin start-domain
```

### Arrêter Payara
```bash
/home/mc/Documents/payara/payara6/bin/asadmin stop-domain
```

### Builder et déployer (1ère fois)
```bash
cd /home/mc/Documents/payara/tp1-charles && ./mvnw clean package && /home/mc/Documents/payara/payara6/bin/asadmin deploy --force=true target/tp1-charles.war
```

### Redéployer après une modification
```bash
cd /home/mc/Documents/payara/tp1-charles && ./mvnw clean package && /home/mc/Documents/payara/payara6/bin/asadmin redeploy --name=tp1-charles target/tp1-charles.war
```

## URLs

| Page | URL |
|------|-----|
| Application | http://localhost:8080/tp1-charles/ |
| Console Payara | http://localhost:4848 |

## Logs en temps réel
```bash
tail -f /home/mc/Documents/payara/payara6/glassfish/domains/domain1/logs/server.log
```
