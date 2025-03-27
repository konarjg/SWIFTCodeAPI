import {React, useRef, useState} from 'react';
import {TopNav} from '../components/TopNav';
import { Sidebar } from '../components/Sidebar';
import { useSidebar } from '../hooks/useSidebar';
import styles from '../styles/AddSwiftCode.module.css';
import { useSwiftCodeService } from '../api/useSwiftCodeService';

export function AddSwiftCode() {
    const [sidebarRef, handleSidebar] = useSidebar();
    const [getSwiftCodeDetails, getSwiftCodesByCountry, addSwiftCode, removeSwiftCode] = useSwiftCodeService();

    const bankNameRef = useRef(null);
    const addressRef = useRef(null);
    const swiftCodeRef = useRef(null);
    const countryISO2Ref = useRef(null);
    const countryNameRef = useRef(null);
    const secretRef = useRef(null);

    const [responseMessage, setResponseMessage] = useState("");

    const handleUppercaseConversion = (inputRef) => {
        const input = inputRef.current.value;

        inputRef.current.value = input.toUpperCase();
    }

    const handleAddSwiftCode = async (event) => {
        event.preventDefault();
        const bankName = bankNameRef.current.value;
        const address = addressRef.current.value;
        const swiftCode = swiftCodeRef.current.value;
        const countryISO2 = countryISO2Ref.current.value;
        const countryName = countryNameRef.current.value;
        const isHeadquarter = swiftCode.endsWith("XXX");

        const secret = secretRef.current.value;

        const swiftCodeData = {
            address: address,
            bankName: bankName,
            countryISO2: countryISO2,
            countryName: countryName,
            isHeadquarter: isHeadquarter,
            swiftCode: swiftCode
        };

        const response = await addSwiftCode(secret, swiftCodeData);
        const message = (response !== null && response.message !== null) ? response.message : "There was an error processing your request!";
        setResponseMessage(message);
    }

    return (
        <main className={styles.page}>
            <Sidebar ref={sidebarRef}/>

            <header className={styles.header}>
                <TopNav handleSidebar={handleSidebar}/>
            </header>

            <section className={styles.content}>
                <form className={styles.addForm}>
                    <label className={styles.responseMessageLabel}>{responseMessage}</label>
                    <label className={styles.addLabel}>Secret API key</label>
                    <input type="password" className={styles.addInput} ref={secretRef} required maxLength="64" minLength="64"/>
                    <label className={styles.addLabel}>Bank name</label>
                    <input type="text" className={styles.addInput} required ref={bankNameRef}/>
                    <label className={styles.addLabel}>Address</label>
                    <input type="text" className={styles.addInput} ref={addressRef}/>
                    <label className={styles.addLabel}>SWIFT code</label>
                    <input type="text" className={styles.addInput} required minLength="11" maxLength="11" ref={swiftCodeRef} onChange={() => handleUppercaseConversion(swiftCodeRef)}/>
                    <label className={styles.addLabel}>Country ISO2 code</label>
                    <input type="text" className={styles.addInput} required maxLength="2" minLength="2" ref={countryISO2Ref} onChange={() => handleUppercaseConversion(countryISO2Ref)}/>
                    <label className={styles.addLabel}>Country name</label>
                    <input type="text" className={styles.addInput} required ref={countryNameRef} onChange={() => handleUppercaseConversion(countryNameRef)}/>
                    <button className={styles.addButton} onClick={(event) => handleAddSwiftCode(event)}>Add SWIFT code</button>
                </form>
            </section>

            <footer className={styles.footer}>This page is part of the Remitly 2025 internship assignment.</footer>
        </main>
    );
}