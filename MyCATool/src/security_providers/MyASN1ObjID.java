package security_providers;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
/**
 *
 */
public class MyASN1ObjID {
    /**
     * Subject Directory Attributes
     * @deprecated use X509Extension value.
     */
    public static final ASN1ObjectIdentifier SubjectDirectoryAttributes = new ASN1ObjectIdentifier("2.5.29.9");
    
    /**
     * Subject Key Identifier
     *  @deprecated use X509Extension value.
     */
    public static final ASN1ObjectIdentifier SubjectKeyIdentifier = new ASN1ObjectIdentifier("2.5.29.14");

    /**
     * Key Usage
     *  @deprecated use X509Extension value.
     */
    public static final ASN1ObjectIdentifier KeyUsage = new ASN1ObjectIdentifier("2.5.29.15");

    /**
     * Private Key Usage Period
     *  @deprecated use X509Extension value.
     */
    public static final ASN1ObjectIdentifier PrivateKeyUsagePeriod = new ASN1ObjectIdentifier("2.5.29.16");

    /**
     * Subject Alternative Name
     *  @deprecated use X509Extension value.
     */
    public static final ASN1ObjectIdentifier SubjectAlternativeName = new ASN1ObjectIdentifier("2.5.29.17");

    /**
     * Issuer Alternative Name
     *  @deprecated use X509Extension value.
     */
    public static final ASN1ObjectIdentifier IssuerAlternativeName = new ASN1ObjectIdentifier("2.5.29.18");

    /**
     * Basic Constraints
     *  @deprecated use X509Extension value.
     */
    public static final ASN1ObjectIdentifier BasicConstraints = new ASN1ObjectIdentifier("2.5.29.19");

    /**
     * CRL Number
     *  @deprecated use X509Extension value.
     */
    public static final ASN1ObjectIdentifier CRLNumber = new ASN1ObjectIdentifier("2.5.29.20");

    /**
     * Reason code
     *  @deprecated use X509Extension value.
     */
    public static final ASN1ObjectIdentifier ReasonCode = new ASN1ObjectIdentifier("2.5.29.21");

    /**
     * Hold Instruction Code
     *  @deprecated use X509Extension value.
     */
    public static final ASN1ObjectIdentifier InstructionCode = new ASN1ObjectIdentifier("2.5.29.23");

    /**
     * Invalidity Date
     *  @deprecated use X509Extension value.
     */
    public static final ASN1ObjectIdentifier InvalidityDate = new ASN1ObjectIdentifier("2.5.29.24");

    /**
     * Delta CRL indicator
     *  @deprecated use X509Extension value.
     */
    public static final ASN1ObjectIdentifier DeltaCRLIndicator = new ASN1ObjectIdentifier("2.5.29.27");

    /**
     * Issuing Distribution Point
     *  @deprecated use X509Extension value.
     */
    public static final ASN1ObjectIdentifier IssuingDistributionPoint = new ASN1ObjectIdentifier("2.5.29.28");

    /**
     * Certificate Issuer
     *  @deprecated use X509Extension value.
     */
    public static final ASN1ObjectIdentifier CertificateIssuer = new ASN1ObjectIdentifier("2.5.29.29");

    /**
     * Name Constraints
     *  @deprecated use X509Extension value.
     */
    public static final ASN1ObjectIdentifier NameConstraints = new ASN1ObjectIdentifier("2.5.29.30");

    /**
     * CRL Distribution Points
     *  @deprecated use X509Extension value.
     */
    public static final ASN1ObjectIdentifier CRLDistributionPoints = new ASN1ObjectIdentifier("2.5.29.31");

    /**
     * Certificate Policies
     *  @deprecated use X509Extension value.
     */
    public static final ASN1ObjectIdentifier CertificatePolicies = new ASN1ObjectIdentifier("2.5.29.32");

    /**
     * Policy Mappings
     *  @deprecated use X509Extension value.
     */
    public static final ASN1ObjectIdentifier PolicyMappings = new ASN1ObjectIdentifier("2.5.29.33");

    /**
     * Authority Key Identifier
     *  @deprecated use X509Extension value.
     */
    public static final ASN1ObjectIdentifier AuthorityKeyIdentifier = new ASN1ObjectIdentifier("2.5.29.35");

    /**
     * Policy Constraints
     *  @deprecated use X509Extension value.
     */
    public static final ASN1ObjectIdentifier PolicyConstraints = new ASN1ObjectIdentifier("2.5.29.36");

    /**
     * Extended Key Usage
     *  @deprecated use X509Extension value.
     */
    public static final ASN1ObjectIdentifier ExtendedKeyUsage = new ASN1ObjectIdentifier("2.5.29.37");

    /**
     * Freshest CRL
     *  @deprecated use X509Extension value.
     */
    public static final ASN1ObjectIdentifier FreshestCRL = new ASN1ObjectIdentifier("2.5.29.46");
     
    /**
     * Inhibit Any Policy
     *  @deprecated use X509Extension value.
     */
    public static final ASN1ObjectIdentifier InhibitAnyPolicy = new ASN1ObjectIdentifier("2.5.29.54");

    /**
     * Authority Info Access
     *  @deprecated use X509Extension value.
     */
    public static final ASN1ObjectIdentifier AuthorityInfoAccess = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.1.1");

    /**
     * Subject Info Access
     *  @deprecated use X509Extension value.
     */
    public static final ASN1ObjectIdentifier SubjectInfoAccess = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.1.11");
    
    /**
     * Logo Type
     *  @deprecated use X509Extension value.
     */
    public static final ASN1ObjectIdentifier LogoType = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.1.12");

    /**
     * BiometricInfo
     *  @deprecated use X509Extension value.
     */
    public static final ASN1ObjectIdentifier BiometricInfo = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.1.2");
    
    /**
     * QCStatements
     *  @deprecated use X509Extension value.
     */
    public static final ASN1ObjectIdentifier QCStatements = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.1.3");

    /**
     * Audit identity extension in attribute certificates.
     *  @deprecated use X509Extension value.
     */
    public static final ASN1ObjectIdentifier AuditIdentity = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.1.4");
    
    /**
     * NoRevAvail extension in attribute certificates.
     *  @deprecated use X509Extension value.
     */
    public static final ASN1ObjectIdentifier NoRevAvail = new ASN1ObjectIdentifier("2.5.29.56");

    /**
     * TargetInformation extension in attribute certificates.
     *  @deprecated use X509Extension value.
     */
    public static final ASN1ObjectIdentifier TargetInformation = new ASN1ObjectIdentifier("2.5.29.55");
}
