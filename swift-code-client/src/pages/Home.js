import React from 'react';
import {TopNav} from '../components/TopNav';
import styles from '../styles/Home.module.css';

export function Home() {
    return (
        <main className={styles.page}>
            <header className={styles.header}>
                <TopNav></TopNav>
            </header>

            <section className={styles.content}>
                testataatatatata
            </section>

            <footer className={styles.footer}>This page is part of the Remitly 2025 internship assignment.</footer>
        </main>
    );
}