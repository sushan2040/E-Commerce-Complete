import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';

import en from '../locales/en.json'
import fr from '../locales/fr.json'; // Add more languages as needed

i18n
    .use(initReactI18next)
    .init({
        resources: {
            en: { translation: en },
            fr: { translation: fr },
        },
        lng: 'en', // Default language
        fallbackLng: 'en', // Fallback language
        interpolation: { escapeValue: false }, // React escapes by default
    });

export default i18n;
