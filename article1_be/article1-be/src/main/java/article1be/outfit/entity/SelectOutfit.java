package article1be.outfit.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "select_outfit")
public class SelectOutfit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long selectOutfitSeq;

    @ManyToOne
    @JoinColumn(name = "outfit_seq", nullable = false)
    private Outfit outfit;

    @ManyToOne
    @JoinColumn(name = "select_seq", nullable = false)
    private SelectRecord selectRecord;

    public static SelectOutfit create(SelectRecord selectRecord, Outfit outfit) {
        SelectOutfit selectOutfit = new SelectOutfit();
        selectOutfit.selectRecord = selectRecord;
        selectOutfit.outfit = outfit;
        return selectOutfit;
    }
}
