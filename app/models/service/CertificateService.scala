package models
package service

import javax.inject.*
import java.security.Security
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.Path
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.math.BigInteger
import java.util.Date

import scala.util.Try
import scala.sys.process.*

import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.openssl.jcajce.JcaPEMWriter
import org.bouncycastle.cert.X509v3CertificateBuilder
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder
import org.bouncycastle.pkcs.jcajce.JcePKCSPBEOutputEncryptorBuilder
import org.bouncycastle.asn1.x500.X500Name
import org.bouncycastle.pkcs.PKCS12PfxPduBuilder
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers
import org.bouncycastle.jcajce.PKCS12Key
import org.bouncycastle.pkcs.jcajce.JcaPKCS12SafeBagBuilder
import org.bouncycastle.asn1.DERBMPString
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo
import org.bouncycastle.pkcs.jcajce.JcePKCS12MacCalculatorBuilder

@Singleton
class CertificateService @Inject() (
) {
  Security.addProvider(new BouncyCastleProvider())

  def generateKeyPair(): Try[Unit] = {
    Try {
      // Initialize RSA key pair generator
      val keyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC")
      keyPairGenerator.initialize(2048)
      val keyPair = keyPairGenerator.generateKeyPair()

      // Extract private and public keys
      val privateKey = keyPair.getPrivate
      val publicKey  = keyPair.getPublic

      // Define output directory and ensure it exists
      val keyDir: Path = Paths.get("public/key")
      Files.createDirectories(keyDir)

      // Save private key (PKCS8 format)
      val privateKeySpec   = new PKCS8EncodedKeySpec(privateKey.getEncoded)
      val privateKeyPath   = keyDir.resolve("private_key.pem")
      val privateKeyOutput = new FileOutputStream(privateKeyPath.toFile)
      try {
        privateKeyOutput.write(privateKeySpec.getEncoded)
      } finally {
        privateKeyOutput.close()
      }

      // Save public key (X509 format)
      val publicKeySpec   = new X509EncodedKeySpec(publicKey.getEncoded)
      val publicKeyPath   = keyDir.resolve("public_key.pem")
      val publicKeyOutput = new FileOutputStream(publicKeyPath.toFile)
      try {
        publicKeyOutput.write(publicKeySpec.getEncoded)
      } finally {
        publicKeyOutput.close()
      }
    }
  }

  def generateP12WithKeytool(
      alias: String,
      keyalg: String = "RSA",
      keysize: String = "2048",
      validity: String = "365",
      storepass: String = "password123",
      orgUnit: String = "IT",
      org: String = "Vauldex",
      locality: String = "Cebu City",
      state: String = "Cebu",
      country: String = "PH"
  ): Try[Unit] = {
    Try {
      // Define output directory and ensure it exists
      val keyDir: Path = Paths.get("public/key")
      Files.createDirectories(keyDir)

      // Define keytool command
      val p12Path = keyDir.resolve("test.p12").toAbsolutePath.toString
      val crtPath = keyDir.resolve("test.crt").toAbsolutePath.toString
      val dname =
        s"CN=$alias, OU=$orgUnit, O=$org, L=$locality, ST=$state, C=$country"
      val keytoolCmd = Seq(
        "keytool",
        "-genkeypair",
        "-alias",
        alias,
        "-keyalg",
        keyalg,
        "-keysize",
        keysize,
        "-validity",
        validity,
        "-keystore",
        p12Path,
        "-storetype",
        "PKCS12",
        "-storepass",
        storepass,
        "-dname",
        dname
      )

      // Execute keytool command
      val exitCode = keytoolCmd.!
      if exitCode != 0 then {
        throw new RuntimeException(
          s"keytool command failed with exit code $exitCode"
        )
      }

      // Verify the .p12 file was created
      if !Files.exists(Paths.get(p12Path)) then {
        throw new RuntimeException(s".p12 file was not created")
      }

      val keytoolExportCmd = Seq(
        "keytool",
        "-exportcert",
        "-alias",
        alias,
        "-keystore",
        p12Path,
        "-storetype",
        "PKCS12",
        "-storepass",
        storepass,
        "-file",
        crtPath,
        "-rfc"
      )
      if keytoolExportCmd.! != 0 then
        throw new RuntimeException("keytool exportcert failed")
      if !Files.exists(Paths.get(crtPath)) then
        throw new RuntimeException(".crt file not created")
    }
  }

  def generateP12WithBouncyCastle(
      alias: String,
      keyalg: String = "RSA",
      keysize: String = "2048",
      validity: String = "365",
      storepass: String = "password123",
      orgUnit: String = "IT",
      org: String = "Vauldex",
      locality: String = "Cebu City",
      state: String = "Cebu",
      country: String = "PH"
  ): Try[Unit] = {
    Try {
      // Validate inputs
      val keySizeInt = keysize.toIntOption.getOrElse(
        throw new IllegalArgumentException("Invalid keysize")
      )
      val validityDays = validity.toIntOption.getOrElse(
        throw new IllegalArgumentException("Invalid validity")
      )

      // Generate key pair
      val keyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC")
      keyPairGenerator.initialize(2048)
      val keyPair    = keyPairGenerator.generateKeyPair()
      val privateKey = keyPair.getPrivate
      val publicKey  = keyPair.getPublic
      val subjectPublicKeyInfo =
        SubjectPublicKeyInfo.getInstance(publicKey.getEncoded)

      // Create self-signed certificate
      val issuer = new X500Name(
        s"CN=$alias, OU=$orgUnit, O=$org, L=$locality, ST=$state, C=$country"
      )
      val serial = BigInteger.valueOf(System.currentTimeMillis())
      val notBefore =
        new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000) // Yesterday
      val notAfter = new Date(
        System.currentTimeMillis() + 356 * 24 * 60 * 60 * 1000L
      ) // Validity days
      val certBuilder = new X509v3CertificateBuilder(
        issuer,
        serial,
        notBefore,
        notAfter,
        issuer,
        subjectPublicKeyInfo
      )
      val signer = new JcaContentSignerBuilder("SHA256withRSA")
        .build(privateKey)
      val certHolder = certBuilder.build(signer)
      val certificate = new JcaX509CertificateConverter()
        .getCertificate(certHolder)

      // Define output directory and ensure it exists
      val keyDir: Path = Paths.get("public/key")
      Files.createDirectories(keyDir)

      // Create PKCS12 (.p12) file
      val pfxBuilder    = new PKCS12PfxPduBuilder()
      val privateKeyBag = new JcaPKCS12SafeBagBuilder(privateKey)
      privateKeyBag.addBagAttribute(
        PKCSObjectIdentifiers.pkcs_9_at_friendlyName,
        new DERBMPString(alias)
      )
      val certBag = new JcaPKCS12SafeBagBuilder(certificate)
      certBag.addBagAttribute(
        PKCSObjectIdentifiers.pkcs_9_at_friendlyName,
        new DERBMPString(alias)
      )

      val outputEncryptor = new JcePKCSPBEOutputEncryptorBuilder(
        PKCSObjectIdentifiers.pbeWithSHAAnd3_KeyTripleDES_CBC
      ).build(storepass.toCharArray)
      pfxBuilder.addEncryptedData(
        outputEncryptor,
        Array(privateKeyBag.build(), certBag.build())
      )

      // val macCalculatorBuilder =
      //   new JcePKCS12MacCalculatorBuilder()
      val pfx      = pfxBuilder.build(null, storepass.toCharArray)
      val pfxBytes = pfx.getEncoded()
      val p12Path  = keyDir.resolve("test.p12")

      val p12Output = new FileOutputStream(p12Path.toFile)
      try {
        p12Output.write(pfxBytes)
      } finally {
        p12Output.close()
      }

      // Save certificate to .crt file in PEM format
      val crtPath = keyDir.resolve("test.crt")
      val crtOutput =
        new OutputStreamWriter(new FileOutputStream(crtPath.toFile))
      val pemWriter = new JcaPEMWriter(crtOutput)
      try {
        pemWriter.writeObject(certificate)
      } finally {
        pemWriter.close()
        crtOutput.close()
      }

      // Verify files exist
      if !Files.exists(p12Path) then {
        throw new RuntimeException(s".p12 file was not created at $p12Path")
      }
      if !Files.exists(crtPath) then {
        throw new RuntimeException(s".crt file was not created at $crtPath")
      }
    }
  }
}
