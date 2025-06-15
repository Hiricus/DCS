package com.hiricus.dcs.service.template.replacing;

public enum ReplacementKeyword {
    NAME,
    SURNAME,
    PATRONYMIC,
    FULL_NAME,
    SUBJECTS_GROUP,
    CURRENT_DATE,
    DATE;

    @Override
    public String toString() {
        return "{" + this.name() + "}";
    }
}
