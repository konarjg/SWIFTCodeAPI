import {React, useRef, useState} from 'react';
import {TopNav} from '../components/TopNav';
import { Sidebar } from '../components/Sidebar';
import { useSidebar } from '../hooks/useSidebar';
import styles from '../styles/GetSwiftCodesByCountry.module.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSearch } from "@fortawesome/free-solid-svg-icons"
import { useSwiftCodeService } from '../api/useSwiftCodeService';
import { BranchCodeDetails } from '../components/BranchCodeDetails';

export function GetSwiftCodesByCountry() {
    const [sidebarRef, handleSidebar] = useSidebar();
    const searchFieldRef = useRef(null);
    const [getSwiftCodeDetails, getSwiftCodesByCountry, addSwiftCode, removeSwiftCode] = useSwiftCodeService();
    const [codes, setCodes] = useState(null);

    function handleSearchChange() {
        const input = searchFieldRef.current.value;

        searchFieldRef.current.value = input.toUpperCase();
    }

    const handleSearch = async (event) => {
        event.preventDefault();
        const countryISO2 = searchFieldRef.current.value;
        const codes = await getSwiftCodesByCountry(countryISO2);
        setCodes(codes);
    }

    return (
        <main className={styles.page}>
            <Sidebar ref={sidebarRef}/>

            <header className={styles.header}>
                <TopNav handleSidebar={handleSidebar}/>
            </header>

            <section className={styles.content}>
                <form className={styles.searchForm}>
                    <label className={styles.searchLabel}>Find SWIFT codes by country ISO2 code</label>
                    <input ref={searchFieldRef} type="text" className={styles.searchField} minLength="11" maxLength="11" onChange={handleSearchChange}/>
                    <button className={styles.searchButton} onClick={(event) => handleSearch(event)}><FontAwesomeIcon icon={faSearch} /></button>
                </form>
                
                {   
                    codes !== null ?
                    <article>
                        <h1>SWIFT Codes in {codes.countryName}</h1>
                        <p>Country name: {codes.countryName}</p>
                        <p>Country ISO2: {codes.countryISO2}</p>

                        {codes.swiftCodes.map(code => <BranchCodeDetails code={code} key={code.swiftCode}/>)}
                    </article>
                    : <p>
                        On this page, you can effortlessly access all SWIFT codes for banks in a specific country using its ISO 2 code.
                        These codes are crucial for international banking transactions, identifying banks and branches worldwide. 
                        To get started, simply type the two-letter ISO 2 country code (e.g., "US" for the United States, "DE" for Germany, or "JP" for Japan) 
                        in the search field located above.
                    </p>
                }
            </section>

            <footer className={styles.footer}>This page is part of the Remitly 2025 internship assignment.</footer>
        </main>
    );
}