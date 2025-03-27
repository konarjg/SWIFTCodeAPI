import {React, useRef, useState} from 'react';
import {TopNav} from '../components/TopNav';
import { Sidebar } from '../components/Sidebar';
import { useSidebar } from '../hooks/useSidebar';
import styles from '../styles/RemoveSwiftCode.module.css';
import { useSwiftCodeService } from '../api/useSwiftCodeService';

export function RemoveSwiftCode() {
    const [sidebarRef, handleSidebar] = useSidebar();
    const [getSwiftCodeDetails, getSwiftCodesByCountry, addSwiftCode, removeSwiftCode] = useSwiftCodeService();

    const swiftCodeRef = useRef(null);
    const secretRef = useRef(null);

    const [responseMessage, setResponseMessage] = useState("");

    const handleUppercaseConversion = (inputRef) => {
        const input = inputRef.current.value;

        inputRef.current.value = input.toUpperCase();
    }

    const handleRemoveSwiftCode = async (event) => {
        event.preventDefault();
        
        const secret = secretRef.current.value;
        const swiftCode = swiftCodeRef.current.value;

        const response = await removeSwiftCode(secret, swiftCode);

        const message = (response !== null && response.message !== null) ? response.message : "There was an error processing your request!";
        setResponseMessage(message);
    };

    return (
        <main className={styles.page}>
            <Sidebar ref={sidebarRef}/>

            <header className={styles.header}>
                <TopNav handleSidebar={handleSidebar}/>
            </header>

            <section className={styles.content}>
                <form className={styles.removeForm}>
                    <label className={styles.responseMessageLabel}>{responseMessage}</label>
                    <label className={styles.removeLabel}>Secret API key</label>
                    <input type="password" className={styles.removeInput} ref={secretRef} required maxLength="64" minLength="64"/>
                    <label className={styles.removeLabel}>SWIFT code to remove</label>
                    <input type="text" className={styles.removeInput} required minLength="11" maxLength="11" ref={swiftCodeRef} onChange={() => handleUppercaseConversion(swiftCodeRef)}/>
                    <button className={styles.removeButton} onClick={(event) => handleRemoveSwiftCode(event)}>Remove SWIFT code</button>
                </form>
            </section>

            <footer className={styles.footer}>This page is part of the Remitly 2025 internship assignment.</footer>
        </main>
    );
}